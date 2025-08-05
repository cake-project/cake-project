package com.cakemate.cake_platform.dummy;

import com.cakemate.cake_platform.common.commonEnum.CakeSize;
import com.cakemate.cake_platform.domain.proposalForm.enums.ProposalFormStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProposalFormBulkInsert {

    private final String DB_URL = "jdbc:mysql://localhost:3306/cake?serverTimezone=UTC&useSSL=false&rewriteBatchedStatements=true";
    private final String USER = "root";
    private final String PASS = "root1234!";
    private final int TOTAL = 1_000_000;
    private final int BATCH_SIZE = 5000;
    private final int THREAD_COUNT = 6;

    private final Random random = new Random();

    public void bulkInsert() {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        int chunkSize = TOTAL / THREAD_COUNT;

        for (int t = 0; t < THREAD_COUNT; t++) {
            final int start = t * chunkSize + 1;
            final int end = (t == THREAD_COUNT - 1) ? TOTAL : start + chunkSize - 1;

            executor.submit(() -> insertRange(start, end));
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            // 대기
        }
    }

    private void insertRange(int start, int end) {
        String sql = "INSERT INTO proposal_forms (request_form_id, store_id, owner_id, store_name, manager_name, " +
                "title, content, proposed_price, image, proposed_pickup_date, created_at, status, " +
                "is_deleted, quantity, cake_size, modified_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        final int TOTAL_OWNERS = 1_000_000; // 예시: 전체 점주 수

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            for (int requestFormId = start; requestFormId <= end; requestFormId++) {
                Set<Long> usedOwnerIds = new HashSet<>();

                for (int j = 0; j < 15; j++) {
                    long ownerId = ((requestFormId * 15 + j) % TOTAL_OWNERS) + 1;

                    if (!usedOwnerIds.add(ownerId)) continue;

                    long storeId = ownerId;
                    String paddedId = String.format("%06d", requestFormId * 15 + j);

                    String storeName = "케이크스토어_" + paddedId;
                    String managerName = "매니저_" + paddedId;
                    String title = "제안서_" + paddedId;
                    String content = "이런 스타일로 제작 가능합니다_" + paddedId;

                    CakeSize cakeSize = CakeSize.getRandom(random);
                    int quantity = random.nextInt(3) + 1;
                    int proposedPrice = cakeSize.getMinPrice() * quantity;

                    String image = "https://example.com/proposal/" + paddedId;
                    LocalDateTime pickupDate = LocalDateTime.now().plusDays(random.nextInt(30));
                    Timestamp pickupTimestamp = Timestamp.valueOf(pickupDate);

                    ProposalFormStatus status = ProposalFormStatus.getRandom(random);
                    Timestamp modifiedAt = Timestamp.valueOf(LocalDateTime.now().plusDays(random.nextInt(10)));

                    pstmt.setLong(1, requestFormId);
                    pstmt.setLong(2, storeId);
                    pstmt.setLong(3, ownerId);
                    pstmt.setString(4, storeName);
                    pstmt.setString(5, managerName);
                    pstmt.setString(6, title);
                    pstmt.setString(7, content);
                    pstmt.setInt(8, proposedPrice);
                    pstmt.setString(9, image);
                    pstmt.setTimestamp(10, pickupTimestamp);
                    pstmt.setTimestamp(11, now);
                    pstmt.setString(12, status.getStrValue());
                    pstmt.setBoolean(13, false);
                    pstmt.setInt(14, quantity);
                    pstmt.setString(15, cakeSize.name());
                    pstmt.setTimestamp(16, modifiedAt);

                    pstmt.addBatch();
                }

                if ((requestFormId - start + 1) % BATCH_SIZE == 0) {
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
}