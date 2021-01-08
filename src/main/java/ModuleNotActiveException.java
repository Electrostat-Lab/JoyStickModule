public class ModuleNotActiveException extends RuntimeException {
    private final String message;
    ModuleNotActiveException(String message){
        this.message=message;
    }
    @Override
    public void printStackTrace() {
        System.err.println(message);
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return super.fillInStackTrace();
    }
}
