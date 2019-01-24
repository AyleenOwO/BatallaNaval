package cl.ucn.disc.dsm.atorres.navalbattle.bluetoothconnection;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import cl.ucn.disc.dsm.atorres.navalbattle.R;
import cl.ucn.disc.dsm.atorres.navalbattle.game.adapters.BoardAdapter;
import cl.ucn.disc.dsm.atorres.navalbattle.game.gameset.Grid;
import cl.ucn.disc.dsm.atorres.navalbattle.game.gameset.Player;
import cl.ucn.disc.dsm.atorres.navalbattle.game.gameset.ShipArrangement;

public class GameFragment extends Fragment {

    private static final String TAG = "BluetoothChatFragment";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    private TextView tvMessage;
    private Button btnConnect, btnRestart;
    private GridView gvBoard1, gvBoard2;
    private BoardAdapter boardAdapter1, boardAdapter2;
    private Player player1;
    private int player = 1;
    private ArrayList<Grid> gridList;
    private Stage stage;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Member object for the chat services
     */
    private BluetoothService mService = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            FragmentActivity activity = getActivity();
            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            activity.finish();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mService == null) {
            setUpGame();
            btnConnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                    arrangeBoard();
                }
            });

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            mService.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth chat services
                mService.start();
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view  =  inflater.inflate(R.layout.main_activity, container, false);

        tvMessage = view.findViewById(R.id.tv_message);
        btnRestart = view.findViewById(R.id.btn_initialize);
        btnConnect = view.findViewById(R.id.btn_connect);
        gvBoard1 = view.findViewById(R.id.gv_one);
        gvBoard2 = view.findViewById(R.id.gv_two);
        boardAdapter1 = new BoardAdapter(getActivity(), new ArrayList<>());
        boardAdapter2 = new BoardAdapter(getActivity(), new ArrayList<>());
        gridList = new ArrayList<>();

        return view;
    }

    private void setUpGame(){

        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpGame();
            }
        });

        disableClicking();

        boardAdapter1.clear();
        boardAdapter2.clear();
        boardAdapter1.addGrids(gvBoard1, 1, getNumGridsBoardArea());
        boardAdapter2.addGrids(gvBoard2, 2, getNumGridsBoardArea());

        player1 = new Player(1);


        // Initialize the BluetoothChatService to perform bluetooth connections
        mService = new BluetoothService(getActivity(), mHandler);

    }

    /**
     * El jugador prepara su tablero
     */
    private void arrangeBoard() {
        btnConnect.setVisibility(View.GONE);
        stage = Stage.ARRAGING;
        setMessage("Posiciona tus barcos tocando tu territorio");
        gvBoard1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Grid grid = (Grid) parent.getAdapter().getItem(position);
                player1.addGrid(grid);
                boardAdapter1.notifyDataSetChanged();

                if (player1.canAddGrid())
                    setMessage(player1.getShips().get(player1.getNumShipsArranged()).getNumGridsLeft() +
                            " casilla(s) quedan para agregar a tu barco " +
                            (player1.getNumShipsArranged() + 1));
                else {
                    gvBoard1.setOnItemClickListener(null);

                    if (checkArrange()){
                        String done = "done";
                        sendPosition(done);}
                        //letP1attack();
                    else
                        setMessage("Posicionamiento invalido, click REINICIAR");
                }

            }
        });
    }

    /**
     * Verifica si la posicion de los barcos tienen un largo valido
     *
     * @return
     */
    private boolean checkArrange() {
        ShipArrangement shipArr = new ShipArrangement();
        BoardAdapter boardAdapter = boardAdapter1;
        int c = 0;
        for (int i = 0; i < boardAdapter.getCount(); i++) {
            Grid grid = boardAdapter1.getItem(i);
            gridList.add(grid);
            if (grid.getStatus() == Grid.Status.OCCUPIED) {
                c = c + 1;
                if (c == 10) {
                    if (((shipArr.checkArrangeLH(boardAdapter)) || (shipArr.checkArrangeLV(boardAdapter)))) {
                        if (((shipArr.checkArrangeMH(boardAdapter)) || (shipArr.checkArrangeMV(boardAdapter)))) {
                            if (((shipArr.checkArrangeSH(boardAdapter)) || (shipArr.checkArrangeSV(boardAdapter)))) {

                                return true;
                            }
                        }
                    }

                }
            }
        }
        gridList.clear();
        return false;
    }

    /**
     * El jugador ataca
     */
    private void letsAttack() {
        btnRestart.setVisibility(View.GONE);
        gvBoard2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Grid grid = (Grid) parent.getAdapter().getItem(position);
                player1.attackGrid(grid);
                boardAdapter2.notifyDataSetChanged();
                String pos = Integer.toString(position);
                sendPosition(pos);

                if (!player1.isAlive()) {
                    disableClicking();
                    setMessage("¡GANASTE! apreta reinciar");
                } else if (player1.canAttack()) {
                    setMessage(("¡TU turno!"));
                } else {
                    gvBoard2.setOnItemClickListener(null);
                    player1.resetAttacks();
                }
            }
        });
    }

    /**
     * @param msg mensaje a mostrar en pantalla
     */
    private void setMessage(String msg) {
        tvMessage.setText(msg);
    }

    /**
     * desactiva lo tactil de los tableros
     */
    private void disableClicking() {
        gvBoard1.setOnItemClickListener(null);
        gvBoard2.setOnItemClickListener(null);
    }

    /**
     * @return area del tablero
     */
    private int getNumGridsBoardArea() {
        return (int) Math.pow(7, 2);
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            setStatus(R.string.title_connecting);
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            btnConnect.setVisibility(View.VISIBLE);
                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    setMessage("turno del oponente");
                    if(writeMessage.equalsIgnoreCase("done"))
                        actionPlayer();
                    player = 1;
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    player = 2;
                    String readMessage = new String(readBuf, 0, msg.arg1);

                    setMessage("¡TU turno!");
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Conectado con "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                        btnConnect.setVisibility(View.GONE);
                        Toast.makeText(activity, "Si te invitaron," +
                                " espera a tu turno para posicionar barcos",
                                Toast.LENGTH_SHORT).show();


                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public void actionPlayer(){
        if (stage == Stage.ARRAGING){
            arrangeBoard();
            for(Grid grid : gridList)
                boardAdapter2.add(grid);
            boardAdapter2.notifyDataSetChanged();
            stage = Stage.BATTLING;
        }
        else {
            for(Grid grid : gridList)
                boardAdapter2.add(grid);
            boardAdapter2.notifyDataSetChanged();
        }

    }
    /**
     * Updates the status on the action bar.
     *
     * @param resId a string resource ID
     */
    private void setStatus(int resId) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(resId);
    }

    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
    private void setStatus(CharSequence subTitle) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }

    private void sendPosition(String message) {
        // Check that we're actually connected before trying anything
        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();

            mService.write(send);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setUpGame();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
        }
    }

    /**
     * Establish connection with other device
     *
     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mService.connect(device, secure);
    }

    /**
     * Estado del juego
     */
    private enum Stage {
        ARRAGING, BATTLING
    }

}
