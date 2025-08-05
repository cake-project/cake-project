package com.cakemate.cake_platform.dummy;

import java.sql.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StoreBulkInsert {

    private final String DB_URL = "jdbc:mysql://localhost:3306/cake?serverTimezone=UTC&useSSL=false&rewriteBatchedStatements=true";
    private final String USER = "root";
    private final String PASS = "root1234!";
    private final int BATCH_SIZE = 5000;
    private final int TOTAL = 100_000;
    private final int THREAD_COUNT = 6;
    private final List<String> regions = Arrays.asList(
            "서울", "경기", "인천", "부산", "대구", "광주", "대전", "울산", "세종",
            "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주"
    );

    private final Set<String> usedBusinessNumbers = Collections.synchronizedSet(new HashSet<>());
    private final Random random = new Random();

    public void bulkInsert() {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        int chunkSize = TOTAL / THREAD_COUNT;

        for (int t = 0; t < THREAD_COUNT; t++) {
            int start = t * chunkSize + 1;
            int end = (t == THREAD_COUNT - 1) ? TOTAL : start + chunkSize - 1;

            executor.submit(() -> insertRange(start, end));
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            // 기다리는 중
        }
    }

    private void insertRange(int start, int end) {
        String sql = "INSERT INTO stores (owner_id, business_name, name, address, business_number, phone_number, image, is_active, is_deleted, created_at, modified_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Timestamp now = Timestamp.from(Instant.now());

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
//		"서울", "경기", "인천", "부산", "대구", "광주", "대전", "울산", "세종",
//		"강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주"
            for (int i = start; i <= end; i++) {
                long ownerId = i;
                String paddedId = String.format("%06d", ownerId);

                // 지역 선택: ownerId에 따라 하나의 지역만 할당
                String region = regions.get((int)((ownerId - 1) % regions.size()));

                String businessName = region + "비즈니스" + paddedId;
                String storeName = region + "매장" + paddedId;
                String address = region + " " + paddedId + "번지";
                String businessNumber = generateUniqueBusinessNumber();
                String phone = "010-" + String.format("%04d", random.nextInt(10000)) + "-" + String.format("%04d", random.nextInt(10000));
                String image = "https://example.com/image/" + paddedId;
                boolean isActive = random.nextBoolean();

                pstmt.setLong(1, ownerId);
                pstmt.setString(2, businessName);
                pstmt.setString(3, storeName);
                pstmt.setString(4, address);
                pstmt.setString(5, businessNumber);
                pstmt.setString(6, phone);
                pstmt.setString(7, image);
                pstmt.setBoolean(8, isActive);
                pstmt.setBoolean(9, false);
                pstmt.setTimestamp(10, now);
                pstmt.setTimestamp(11, now);

                pstmt.addBatch();

                if ((i - start + 1) % BATCH_SIZE == 0) {
                    pstmt.executeBatch();
                    conn.commit();
                }
            }

            pstmt.executeBatch();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String generateUniqueBusinessNumber() {
        String businessNumber;
        do {
            String part1 = String.format("%03d", random.nextInt(1000));
            String part2 = String.format("%02d", random.nextInt(100));
            String part3 = String.format("%05d", random.nextInt(100000));
            businessNumber = part1 + "-" + part2 + "-" + part3;
        } while (!usedBusinessNumbers.add(businessNumber)); // 중복이면 다시 생성

        return businessNumber;
    }
}