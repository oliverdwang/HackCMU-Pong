package w.pongrender;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends TiltActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // This is Richard's onCreate space
        MY_UUID = UUID.fromString("7e3c2295-dc1b-4419-a424-50110c0df0d4");
        connectBluetooth();



        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

//        Log.d("Tilt Pitch", Float.toString(pitch)); //You can directly pass pitch. Pitch is for up down.
//        Log.d("Tilt Roll", Float.toString(roll)); //You can directly pass roll. Roll is for left right.

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(new GameView(this));
    }

    public static float getPitch() {
        return pitch;
    }

    public static float getRoll() {
        return roll;
    }


    // Stuff from Richard

    // This is data for William
    public float tilt1 = 0;
    public float tilt2 = 0;
    public float[] renderArray;
    int winDigit = 0;



    // The following are four sending methods for William
    public void askGetTilt() {
        byte[] arr = new byte[30];
        arr[0] = 1;
        connectedThread.write(arr);
    }

    public void sendGetTilt(float f1, float f2) {
        byte[] arr = new byte[30];
        arr[0] = 2;
        byte[] f1Arr = ByteBuffer.allocate(4).putFloat(f1).array();
        byte[] f2Arr = ByteBuffer.allocate(4).putFloat(f2).array();
        for (int i = 0; i < 4; i++) {
            arr[i + 1] = f1Arr[i];
            arr[i + 5] = f2Arr[i];
        }
        connectedThread.write(arr);
    }

    public void sendRender(float f1, float f2, float f3, float f4, float f5, float f6, float f7) {
        byte[] arr = new byte[30];
        arr[0] = 3;
        byte[] f1Arr = ByteBuffer.allocate(4).putFloat(f1).array();
        byte[] f2Arr = ByteBuffer.allocate(4).putFloat(f2).array();
        byte[] f3Arr = ByteBuffer.allocate(4).putFloat(f2).array();
        byte[] f4Arr = ByteBuffer.allocate(4).putFloat(f2).array();
        byte[] f5Arr = ByteBuffer.allocate(4).putFloat(f2).array();
        byte[] f6Arr = ByteBuffer.allocate(4).putFloat(f2).array();
        byte[] f7Arr = ByteBuffer.allocate(4).putFloat(f2).array();
        for (int i = 0; i < 4; i++) {
            arr[i + 1] = f1Arr[i];
            arr[i + 5] = f2Arr[i];
            arr[i + 9] = f3Arr[i];
            arr[i + 13] = f4Arr[i];
            arr[i + 17] = f5Arr[i];
            arr[i + 21] = f6Arr[i];
            arr[i + 25] = f7Arr[i];
        }
        connectedThread.write(arr);
    }

    public void sendWin(int singleDigit) {
        byte[] arr = new byte[30];
        arr[0] = 4;
        arr[1] = (byte) singleDigit;
        connectedThread.write(arr);
    }



    // This is stuff Richard will mess with


    // THIS IS RECEIVER
    // stuff for actual bluetooth communication
    //private Handler mHandler; // handler that gets info from Bluetooth service
    // this leak stuff may or may not cause problems
    // this is where the messages are received
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //TextView receiver = findViewById(R.id.receiver);

            byte[] bytes = (byte[]) msg.obj;
            //textView.setText(bytes.toString());

            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            if (bytes[0] == 1) {

            }
            else if (bytes[0] == 2) {
                tilt1 = buffer.getFloat(1);
                tilt2 = buffer.getFloat(5);
            }
            else if (bytes[0] == 3) {
                renderArray = new float[7];
                for (int i = 0; i < 7; i++) {
                    renderArray[i] = buffer.getFloat(1 + 4 * i);
                }

            }
            else if (bytes[0] == 4) {
                winDigit = bytes[1];
            }
            //receiver.setText("message has been handled:" + "tilt1: " + tilt1 + "tilt2: " + tilt2);

        }
    };



    // This is Richard's space, for bluetooth stuff
    private static int REQUEST_ENABLE_BT = 1;
    private static final String TAG = "mygame";
    private BluetoothAdapter mBluetoothAdapter;
    private UUID MY_UUID;
    private String NAME = "Pong";
    //private String BLUETOOTH_ADDRESS = "14:56:8E:BB:23:F1";
    private String BLUETOOTH_ADDRESS = "A0:CB:FD:12:2A:C5";
    private ConnectedThread connectedThread;



    protected void connectBluetooth() {

        // setup bluetooth
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // since no already paired devices, this part is useless
        // pair devices
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }

    }


    public void startConnecting(boolean isHost) {
        if (isHost) {
            AcceptThread acceptThread = new AcceptThread();
            acceptThread.start();
        }
        else {
            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(BLUETOOTH_ADDRESS);
            ConnectThread connectThread = new ConnectThread(device);
            connectThread.start();
        }
    }


    private class AcceptThread extends Thread {

        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket
            // because mmServerSocket is final.
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code.
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's listen() method failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned.
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "Socket's accept() method failed", e);
                    break;
                }

                if (socket != null) {
                    // A connection was accepted. Perform work associated with
                    // the connection in a separate thread.
                    manageMyConnectedSocket(socket);
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        Log.e(TAG, "socket close() method failed");
                        break;
                    }
                    break;
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }



    // for the client to connect
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
            } catch (Exception connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            manageMyConnectedSocket(mmSocket);
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }



    private void manageMyConnectedSocket(BluetoothSocket socket) {
        //Log.v(TAG, "yay connected");

        //textView.setText("success!");

        connectedThread = new ConnectedThread(socket);
        connectedThread.start();

    }



    // Defines several constants used when transmitting messages between the
    // service and the UI.
    private interface MessageConstants {
        public static final int MESSAGE_READ = 0;
        public static final int MESSAGE_WRITE = 1;
        public static final int MESSAGE_TOAST = 2;

        // ... (Add other message types here as needed.)
    }



    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating output stream", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            mmBuffer = new byte[30];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    // Read from the InputStream.
                    numBytes = mmInStream.read(mmBuffer);
                    // Send the obtained bytes to the UI activity.
                    Message readMsg = mHandler.obtainMessage(
                            MessageConstants.MESSAGE_READ, numBytes, -1,
                            mmBuffer);
                    readMsg.sendToTarget();
                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break;
                }
            }
        }

        // Call this from the main activity to send data to the remote device.
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);

                // Share the sent message with the UI activity.
                Message writtenMsg = mHandler.obtainMessage(
                        MessageConstants.MESSAGE_WRITE, -1, -1, mmBuffer);
                writtenMsg.sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data", e);

                // Send a failure message back to the activity.
                Message writeErrorMsg =
                        mHandler.obtainMessage(MessageConstants.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString("toast",
                        "Couldn't send data to the other device");
                writeErrorMsg.setData(bundle);
                mHandler.sendMessage(writeErrorMsg);
            }
        }

        // Call this method from the main activity to shut down the connection.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }














}
