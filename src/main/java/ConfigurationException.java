public class ConfigurationException extends RuntimeException{
    private final String message;
    public ConfigurationException(String message){
        this.message=message;
    }

    @Override
    public void printStackTrace() {
        System.err.println(message);
    }
}
