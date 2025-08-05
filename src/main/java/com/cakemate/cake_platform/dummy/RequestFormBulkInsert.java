package com.cakemate.cake_platform.dummy;

import com.cakemate.cake_platform.common.commonEnum.CakeSize;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RequestFormBulkInsert {

    private final String DB_URL = "jdbc:mysql://localhost:3306/cake?serverTimezone=UTC&useSSL=false&rewriteBatchedStatements=true";
    private final String USER = "root";
    private final String PASS = "root1234!";
    private final int TOTAL = 1_000_000;
    private final int BATCH_SIZE = 5000;
    private final int THREAD_COUNT = 6;


    private final List<String> regions = Arrays.asList(
            "서울", "경기", "인천", "부산", "대구", "광주", "대전", "울산", "세종",
            "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주"
    );

    private final Random random = new Random();

    public void bulkInsert() {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        int chunkSize = TOTAL / THREAD_COUNT;

        for (int t = 0; t < THREAD_COUNT; t++) {
            final int threadIndex = t;
            final int start = threadIndex * chunkSize + 1;
            final int end = (threadIndex == THREAD_COUNT - 1) ? TOTAL : start + chunkSize - 1;

            executor.submit(() -> insertRange(start, end));
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            // 대기
        }
    }

    private void insertRange(int start, int end) {
        String sql = "INSERT INTO request_forms (customer_id, title, region, content, desired_price, image, desired_pickup_date, status, created_at, is_deleted, quantity, cake_size) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Timestamp now = Timestamp.from(Instant.now());

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            for (int customerId = start; customerId <= end; customerId++) {
                for (int j = 0; j < 20; j++) {
                    String paddedId = String.format("%06d", customerId * 20 + j); // 고유 ID 생성
                    String title = "케이크 요청 " + paddedId;
                    String region = regions.get(random.nextInt(regions.size()));
                    String content = "이런 스타일의 케이크를 원해요_" + paddedId;
                    CakeSize cakeSize = CakeSize.getRandom(random);
                    int quantity = random.nextInt(3) + 1;
                    int desiredPrice = cakeSize.getMinPrice() * quantity;
                    String image = "https://example.com/request/" + paddedId;
                    LocalDateTime pickupDate = LocalDateTime.now().plusDays(random.nextInt(30));
                    Timestamp pickupTimestamp = Timestamp.valueOf(pickupDate);
                    String status = "REQUESTED";

                    pstmt.setLong(1, customerId);
                    pstmt.setString(2, title);
                    pstmt.setString(3, region);
                    pstmt.setString(4, content);
                    pstmt.setInt(5, desiredPrice);
                    pstmt.setString(6, image);
                    pstmt.setTimestamp(7, pickupTimestamp);
                    pstmt.setString(8, status);
                    pstmt.setTimestamp(9, now);
                    pstmt.setBoolean(10, false);
                    pstmt.setInt(11, quantity);
                    pstmt.setString(12, cakeSize.name());

                    pstmt.addBatch();

                    if (((customerId - start) * 20 + j + 1) % BATCH_SIZE == 0) {
                        pstmt.executeBatch();
                        conn.commit();
                    }
                }
            }

            pstmt.executeBatch();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}