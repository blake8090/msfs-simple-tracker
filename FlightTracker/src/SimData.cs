using Microsoft.FlightSimulator.SimConnect;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;

enum DATA_REQUESTS
{
    DataRequest,
    SimEnvironmentReq,
}

enum DEFINITIONS
{
    SimPlaneDataStructure
}

[StructLayout(LayoutKind.Sequential, CharSet = CharSet.Ansi, Pack = 1)]
struct SimPlaneData
{
    [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 256)]
    public String title;
    public double latitude;
    public double longitude;
    public double altitude;
};
