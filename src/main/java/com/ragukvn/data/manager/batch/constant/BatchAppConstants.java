package com.ragukvn.data.manager.batch.constant;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class BatchAppConstants {
    // instead of interface, using final class with private constructor to avoid java:S1214 maintainability rule
    // keeping this class final and private to prevent instantiation

    public static final String VALID_FIELD_ORDER = "ACCOUNT_NUMBER|TRX_AMOUNT|DESCRIPTION|TRX_DATE|TRX_TIME|CUSTOMER_ID";
    public static final String ACCOUNT_NUMBER = "ACCOUNT_NUMBER";
    public static final String TRX_AMOUNT = "TRX_AMOUNT";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String TRX_DATE = "TRX_DATE";
    public static final String TRX_TIME = "TRX_TIME";
    public static final String CUSTOMER_ID = "CUSTOMER_ID";
    public static final String LOAD_STEP_NAME = "file-load-step";
    public static final String READER_NAME = "transactionItemReader";

    public static final int LINES_TO_SKIP = 1;
}
