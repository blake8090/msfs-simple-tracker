using System.Runtime.InteropServices;

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
