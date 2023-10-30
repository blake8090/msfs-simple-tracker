using FlightTracker;

Client client = new();
client.Start();

SimConnectClient simConnectClient = new();
//while (true)
//{
//    var success = simConnectClient.Start();
//    if (success)
//    {
//        break;
//    }
//    else
//    {
//        Console.WriteLine("SimConnect not started. Retrying after 1 second");
//        Thread.Sleep(1000);
//    }
//}

Console.WriteLine("Starting client thread");
Thread clientThread = new(() =>
{
    Console.WriteLine("Running client");
    while (true)
    {
        var message = client.Read();
        Console.WriteLine($"Received message: '{message}'");
        if (message == "STOP")
        {
            break;
        }
    }
});
clientThread.Start();

clientThread.Join();
client.Stop();
simConnectClient.Stop();
