package com.bajaj.qualifier.constant;

public final class ChallengeConstants {

    private ChallengeConstants() {
        // Prevent instantiation
    }

    public static final String DEFAULT_BASE_URL = "https://bfhldevapigw.healthrx.co.in";
    public static final String GENERATE_WEBHOOK_PATH = "/hiring/generateWebhook/JAVA";

    public static final String QUESTION_1_SQL = 
            "SELECT\n" +
            "    p.amount AS SALARY,\n" +
            "    CONCAT(e.first_name, ' ', e.last_name) AS NAME,\n" +
            "    TIMESTAMPDIFF(YEAR, e.dob, CURDATE()) AS AGE,\n" +
            "    d.department_name AS DEPARTMENT_NAME\n" +
            "FROM payments p\n" +
            "JOIN employee e ON p.emp_id = e.emp_id\n" +
            "JOIN department d ON e.department = d.department_id\n" +
            "WHERE DAY(p.payment_time) <> 1\n" +
            "ORDER BY p.amount DESC\n" +
            "LIMIT 1;";

    public static final String QUESTION_2_SQL = 
            "SELECT\n" +
            "    e1.emp_id,\n" +
            "    e1.first_name,\n" +
            "    e1.last_name,\n" +
            "    d.department_name,\n" +
            "    COUNT(e2.emp_id) AS younger_employees_count\n" +
            "FROM employee e1\n" +
            "JOIN department d\n" +
            "    ON e1.department = d.department_id\n" +
            "LEFT JOIN employee e2\n" +
            "    ON e1.department = e2.department\n" +
            "    AND e2.dob > e1.dob\n" +
            "GROUP BY\n" +
            "    e1.emp_id,\n" +
            "    e1.first_name,\n" +
            "    e1.last_name,\n" +
            "    d.department_name\n" +
            "ORDER BY e1.emp_id DESC;";
}
