using FlightTracker;

Server server = new();
server.Start();

while (true)
{
    var message = "Test message";
    server.Send(message);
    Thread.Sleep(3000);
}
