using System.Net;
using System.Net.Sockets;
using System.Text;

namespace FlightTracker
{
    internal class Server
    {
        private const int PORT = 9001;

        private Socket listener;
        private Socket? handler;

        public Server()
        {
            var endPoint = new IPEndPoint(IPAddress.Any, PORT);
            listener = new(endPoint.AddressFamily, SocketType.Stream, ProtocolType.Tcp);
            listener.Bind(endPoint);
            listener.Listen(100);
        }

        public void Start()
        {
            Console.Write("Waiting for a connection... ");
            handler = listener.Accept();
            Console.WriteLine("Connected!");
        }

        public void Stop()
        {
            listener.Close();
            if (handler != null)
            {
                handler.Close();
            }
        }

        public void Send(string message)
        {
            if (handler != null)
            {
                handler.Send(Encoding.UTF8.GetBytes(message), 0);
                Console.WriteLine($"Sent message: {message}");
            }
        }
    }
}
