using System.Net;
using System.Net.Sockets;
using System.Text;

namespace FlightTracker
{
    internal class Client
    {
        private const int PORT = 9001;

        private readonly IPEndPoint endPoint;
        private readonly Socket client;

        public Client()
        {
            endPoint = new(IPAddress.Parse("127.0.0.1"), PORT);
            client = new(endPoint.AddressFamily, SocketType.Stream, ProtocolType.Tcp);
            
        }

        public void Start()
        {
            Console.WriteLine("Waiting for connection...");
            client.Connect(endPoint);
            Console.WriteLine("Connected");
        }

        public void Stop()
        {
            client.Shutdown(SocketShutdown.Both);
        }

        public string Read()
        {
            // Receive ack.
            var buffer = new byte[1_024];
            var received = client.Receive(buffer, SocketFlags.None);
            return Encoding.UTF8.GetString(buffer, 0, received);
            //if (response == "<|ACK|>")
            //{
            //    Console.WriteLine(
            //        $"Socket client received acknowledgment: \"{response}\"");
            //    break;
            //}
        }
    }
}
