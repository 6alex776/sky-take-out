package com.smartdine.exception;

/**
 * 账号被锁定异�?
 */
public class AccountLockedException extends BaseException {

    public AccountLockedException() {
    }

    public AccountLockedException(String msg) {
        super(msg);
    }

}
