using System.Net;
using System.Net.Sockets;
using System.Text;

const int PORT = 9001;

var endPoint = new IPEndPoint(IPAddress.Any, PORT);
using Socket listener = new(
    endPoint.AddressFamily,
    SocketType.Stream,
    ProtocolType.Tcp
);

listener.Bind(endPoint);
listener.Listen(100);

Console.Write("Waiting for a connection... ");
var handler = await listener.AcceptAsync();
Console.WriteLine("Connected!");

while (true)
{
    Console.WriteLine("sending message");
    var message = "Test message";
    await handler.SendAsync(Encoding.UTF8.GetBytes(message), 0);
    Console.WriteLine("message sent");
    Thread.Sleep(10000);
}
