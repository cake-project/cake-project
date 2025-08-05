package com.cakemate.cake_platform.dummy;

import java.sql.*;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProposalFormCommentBulkInsert {

    private final String DB_URL = "jdbc:mysql://localhost:3306/cake?serverTimezone=UTC&useSSL=false&rewriteBatchedStatements=true";
    private final String USER = "root";
    private final String PASS = "root1234!";
    private final int TOTAL_FORMS = 150_000;
    private final int COMMENTS_PER_TYPE = 10; // 고객 10개 + 점주 10개
    private final int BATCH_SIZE = 5000;
    private final int THREAD_COUNT = 6;

    private final Random random = new Random();

    public void bulkInsert() {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        int chunkSize = TOTAL_FORMS / THREAD_COUNT;

        for (int t = 0; t < THREAD_COUNT; t++) {
            final int start = t * chunkSize + 1;
            final int end = (t == THREAD_COUNT - 1) ? TOTAL_FORMS : start + chunkSize - 1;

            executor.submit(() -> insertRange(start, end));
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            // 대기
        }
    }

    private void insertRange(int start, int end) {
        String sql = "INSERT INTO proposal_form_comments (proposal_form_id, customer_id, owner_id, content, is_deleted, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        Timestamp now = Timestamp.from(Instant.now());

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            for (int formId = start; formId <= end; formId++) {
                long ownerId = formId; // 점주 ID는 proposal_form_id와 동일하다고 가정

                // 고객 댓글 10개
                for (int i = 0; i < COMMENTS_PER_TYPE; i++) {
                    long customerId = random.nextInt(1_000_000) + 1;
                    String content = "고객 댓글_" + formId + "_" + i;

                    pstmt.setLong(1, formId);
                    pstmt.setLong(2, customerId);
                    pstmt.setNull(3, Types.BIGINT); // owner_id = NULL
                    pstmt.setString(4, content);
                    pstmt.setBoolean(5, false);
                    pstmt.setTimestamp(6, now);
                    pstmt.addBatch();
                }

                // 점주 댓글 10개
                for (int i = 0; i < COMMENTS_PER_TYPE; i++) {
                    String content = "점주 댓글_" + formId + "_" + i;

                    pstmt.setLong(1, formId);
                    pstmt.setNull(2, Types.BIGINT); // customer_id = NULL
                    pstmt.setLong(3, ownerId);
                    pstmt.setString(4, content);
                    pstmt.setBoolean(5, false);
                    pstmt.setTimestamp(6, now);
                    pstmt.addBatch();
                }

                if ((formId - start + 1) % BATCH_SIZE == 0) {
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