package survivalgame.config;

public class Config extends JsonConfiguration {

    public Server server = new Server();
    
    public static class Server {
        public int port = 4243;
        public int maxConnections = 100;
    }
}
