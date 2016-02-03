package com.ailk.ecs.esf.base.exception;

/**
 * Exception for ECS FrameWork.
 * 
 * @author BingooHuang
 * 
 */
public class EsfException extends RuntimeException {
    /**
     * ctor.
     * @param message 异常消息    
     */
    public EsfException(String message) {
        super(message);
    }

}
