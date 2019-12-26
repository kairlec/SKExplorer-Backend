package com.kairlec.exception;


import com.kairlec.local.utils.ResponseDataUtils;

import java.util.Objects;

public class SKException extends RuntimeException {

    private ServiceError serviceError = null;

    /**
     * 无参默认构造UNSPECIFIED
     */
    public SKException() {
        super();
    }

    /**
     * 由业务错误ServiceError引发
     */
    public SKException(ServiceError serviceError) {
        super(serviceError.getMessage());
        this.serviceError = serviceError;
    }

    /**
     * 指定详细描述构造通用异常
     *
     * @param detailedMessage 详细描述
     */
    public SKException(final String detailedMessage) {
        super(detailedMessage);
    }

    /**
     * 指定导火索构造通用异常
     *
     * @param t 导火索
     */
    public SKException(final Throwable t) {
        super(t);
    }

    /**
     * 构造通用异常
     *
     * @param detailedMessage 详细描述
     * @param t               导火索
     */
    public SKException(final String detailedMessage, final Throwable t) {
        super(detailedMessage, t);
    }

    /**
     * Getter method for property <tt>errorCode</tt>.
     *
     * @return property value of errorCode
     */
    public ServiceError getServiceError() {
        return Objects.requireNonNullElseGet(serviceError, () -> ResponseDataUtils.fromException(this));
    }

}