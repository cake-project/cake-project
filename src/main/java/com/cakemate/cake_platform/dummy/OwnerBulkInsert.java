package com.cakemate.cake_platform.dummy;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.*;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OwnerBulkInsert {

    private final String DB_URL = "jdbc:mysql://localhost:3306/cake?serverTimezone=UTC&useSSL=false&rewriteBatchedStatements=true";
    private final String USER = "root";
    private final String PASS = "root1234!";
    private final int TOTAL = 200_000;
    private final int BATCH_SIZE = 5000; // ✅ 배치 크기 증가
    private final int THREAD_COUNT = 6;

    private final PasswordEncoder encoder;
    private final Random random = new Random();

    public OwnerBulkInsert(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

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
            // 대기
        }
    }

    private void insertRange(int start, int end) {
        String sql = "INSERT INTO owners (email, password, password_confirm, name, phone_number, created_at, modified_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        // ✅ 비밀번호와 시간은 반복문 밖에서 한 번만 생성
        String encodedPassword = encoder.encode("password123");
        Timestamp now = Timestamp.from(Instant.now());

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            for (int i = start; i <= end; i++) {
                String paddedId = String.format("%06d", i);
                String email = "ownerTest" + paddedId + "@gmail.com";
                String name = "점주" + paddedId;
                String phone = "010-" + String.format("%04d", random.nextInt(10000)) + "-" + String.format("%04d", random.nextInt(10000));

                pstmt.setString(1, email);
                pstmt.setString(2, encodedPassword);
                pstmt.setString(3, encodedPassword);
                pstmt.setString(4, name);
                pstmt.setString(5, phone);
                pstmt.setTimestamp(6, now);
                pstmt.setTimestamp(7, now);

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
}


