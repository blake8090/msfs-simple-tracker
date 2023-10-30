using Microsoft.FlightSimulator.SimConnect;
using System.Diagnostics;
using System.Runtime.InteropServices;

namespace FlightTracker
{
    internal class SimConnectClient
    {
        private const int WM_USER_SIMCONNECT = 0x0402;

        private SimConnect? simConnect;

        public Boolean Start()
        {
            try
            {
                Console.WriteLine("Starting simconnect");
                simConnect = new SimConnect("Managed Data Request", Process.GetCurrentProcess().MainWindowHandle, WM_USER_SIMCONNECT, null, 0);
                Console.WriteLine("SimConnect has been started");

                simConnect.AddToDataDefinition(DEFINITIONS.SimPlaneDataStructure, "Title", null, SIMCONNECT_DATATYPE.STRING256, 0, SimConnect.SIMCONNECT_UNUSED);
                simConnect.AddToDataDefinition(DEFINITIONS.SimPlaneDataStructure, "Plane Latitude", "degrees", SIMCONNECT_DATATYPE.FLOAT64, 0, SimConnect.SIMCONNECT_UNUSED);
                simConnect.AddToDataDefinition(DEFINITIONS.SimPlaneDataStructure, "Plane Longitude", "degrees", SIMCONNECT_DATATYPE.FLOAT64, 0, SimConnect.SIMCONNECT_UNUSED);
                simConnect.AddToDataDefinition(DEFINITIONS.SimPlaneDataStructure, "Plane Altitude", "feet", SIMCONNECT_DATATYPE.FLOAT64, 0, SimConnect.SIMCONNECT_UNUSED);
                simConnect.RegisterDataDefineStruct<SimPlaneData>(DEFINITIONS.SimPlaneDataStructure);

                simConnect.OnRecvSimobjectData += new SimConnect.RecvSimobjectDataEventHandler(ReceiveData);
                simConnect.OnRecvSimobjectDataBytype += new SimConnect.RecvSimobjectDataBytypeEventHandler(ReceiveData2);

                Console.WriteLine("requesting data");
                simConnect.RequestDataOnSimObject(DATA_REQUESTS.DataRequest, DEFINITIONS.SimPlaneDataStructure, SimConnect.SIMCONNECT_OBJECT_ID_USER, SIMCONNECT_PERIOD.SECOND, SIMCONNECT_DATA_REQUEST_FLAG.DEFAULT, 0, 0, 0);

                return true;
            }
            catch (COMException e)
            {
                Console.WriteLine($"COMException: {e.Message}");
                return false;
            }
        }

        public void Stop()
        {
            if (simConnect != null)
            {
                simConnect.Dispose();
                Console.WriteLine("SimConnect has been disposed");
            }
        }

        private void ReceiveData(SimConnect sender, SIMCONNECT_RECV_SIMOBJECT_DATA data)
        {
            Console.WriteLine("Received data");

            if ((DATA_REQUESTS)data.dwRequestID == DATA_REQUESTS.DataRequest)
            {
                SimPlaneData planeData = (SimPlaneData)data.dwData[0];
                Console.WriteLine($"Plane title: {planeData.title}");
                Console.WriteLine($"Plane latitude: {planeData.latitude}");
                Console.WriteLine($"Plane longitude: {planeData.longitude}");
                Console.WriteLine($"Plane altitude: {planeData.altitude}");
            }
        }

        private void ReceiveData2(SimConnect sender, SIMCONNECT_RECV_SIMOBJECT_DATA_BYTYPE data)
        {
            Console.WriteLine("Received data2");
        }
    }
}
