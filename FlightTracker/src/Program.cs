using Microsoft.FlightSimulator.SimConnect;
using System.Diagnostics;
using System.Runtime.InteropServices;

const int WM_USER_SIMCONNECT = 0x0402;

Console.WriteLine("Hello, World!");

SimConnect? simConnect = null;

try
{
    simConnect = new SimConnect("Managed Data Request", Process.GetCurrentProcess().MainWindowHandle, WM_USER_SIMCONNECT, null, 0);
    Console.WriteLine("SimConnect has been started");
}
catch (COMException)
{

}

if (simConnect != null)
{
    simConnect.Dispose();
    Console.WriteLine("disposed");
}
