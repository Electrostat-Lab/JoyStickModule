import com.pi4j.gpio.extension.mcp.MCP3008GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3008Pin;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.spi.SpiChannel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class JoystickModule {
    private OnForwardListener onForwardListener;
    private OnBackwardListener onBackwardListener;
    private SteerLTListener steerLTListener;
    private SteerRTListener steerRTListener;
    private NeutralizeListener neutralizeListener;
    private OnClickListener onClickListener;
    private final ArrayList<GpioPinAnalogInput> inputChannels=new ArrayList<>();
    private final SpiChannel rpIchipSelect;
    private GpioController gpioController;
    private MCP3008GpioProvider mcp3008GpioProvider;
    private GpioPinDigitalInput gpioPinDigitalInput;
    public enum Channels {CH_0,CH_1,CH_2,CH_3,CH_4,CH_5,CH_6,CH_7}
    private static final int tolerance=40;
    private static final int neutralizeState=512;

    public JoystickModule(SpiChannel rpIchipSelect){
        this.rpIchipSelect=rpIchipSelect;
    }
    public void registerChannel(Channels channel){
        if(gpioController==null || mcp3008GpioProvider==null){
            throw new ModuleNotActiveException("Module not active or not initialized");
        }
        switch (channel){
            case CH_0:
                inputChannels.add(gpioController.provisionAnalogInputPin(mcp3008GpioProvider, MCP3008Pin.CH0,"Channel0"));
                break;
            case CH_1:
                inputChannels.add(gpioController.provisionAnalogInputPin(mcp3008GpioProvider, MCP3008Pin.CH1,"Channel1"));
                break;
            case CH_2:
                inputChannels.add(gpioController.provisionAnalogInputPin(mcp3008GpioProvider, MCP3008Pin.CH2,"Channel2"));
                break;
            case CH_3:
                inputChannels.add(gpioController.provisionAnalogInputPin(mcp3008GpioProvider, MCP3008Pin.CH3,"Channel3"));
                break;
            case CH_4:
                inputChannels.add(gpioController.provisionAnalogInputPin(mcp3008GpioProvider, MCP3008Pin.CH4,"Channel4"));
                break;
            case CH_5:
                inputChannels.add(gpioController.provisionAnalogInputPin(mcp3008GpioProvider, MCP3008Pin.CH5,"Channel5"));
                break;
            case CH_6:
                inputChannels.add(gpioController.provisionAnalogInputPin(mcp3008GpioProvider, MCP3008Pin.CH6,"Channel6"));
                break;
            case CH_7:
                inputChannels.add(gpioController.provisionAnalogInputPin(mcp3008GpioProvider, MCP3008Pin.CH7,"Channel7"));
                break;
        }
    }
    public void registerAllChannels(){
        if(gpioController==null || mcp3008GpioProvider==null){
            throw new ModuleNotActiveException("Module not active or not initialized");
        }
        LinkedList<Pin> mcp3008Pins= (LinkedList<Pin>) Arrays.asList(MCP3008Pin.ALL);
        mcp3008Pins.forEach(pin -> inputChannels.add(gpioController.provisionAnalogInputPin(mcp3008GpioProvider,pin,pin.getAddress()+"")));
    }
    public void initializeModule(double thresholdAnalogValue,Pin SW_Pin) throws IOException {
        gpioController= GpioFactory.getInstance();
        mcp3008GpioProvider=new MCP3008GpioProvider(rpIchipSelect);
        GpioPinAnalogInput[] gpioPinAnalogInput= new GpioPinAnalogInput[inputChannels.size()];
        /*a way of making the var of the number a final value to be used anonymously & to change it at the same time*/
        final int[] position = {0};
        inputChannels.forEach(channel->{
            gpioPinAnalogInput[position[0]]= channel;
            position[0] +=1;
        });
        mcp3008GpioProvider.setEventThreshold(thresholdAnalogValue, gpioPinAnalogInput);
        mcp3008GpioProvider.setMonitorEnabled(true);
        mcp3008GpioProvider.setMonitorInterval(250);
        gpioPinDigitalInput=gpioController.provisionDigitalInputPin(SW_Pin,PinPullResistance.PULL_DOWN);
        gpioPinDigitalInput.setShutdownOptions(true);

    }
    public void startCollectingChannelsData(){
        if(gpioController==null || mcp3008GpioProvider==null){
            throw new ModuleNotActiveException("Module not active or not initialized");
        }

        GpioPinAnalogInput[] gpioPinAnalogInput= new GpioPinAnalogInput[inputChannels.size()];
        /*a way of making the var of the number a final value to be used anonymously & to change it at the same time*/
        final int[] position = {0};
        inputChannels.forEach(channel->{
            gpioPinAnalogInput[position[0]]= channel;
            position[0] +=1;
        });

        gpioController.addListener((GpioPinListenerAnalog) event -> {

            double valueY=mcp3008GpioProvider.getValue(MCP3008Pin.CH0);
            double valueX=mcp3008GpioProvider.getValue(MCP3008Pin.CH1);

            /*512+40=552*/
            if(valueY>(neutralizeState+tolerance)){
               onForwardListener.forward((float) valueY/100f);
            }
            /*512-40=472*/
            if(valueY<(neutralizeState-tolerance)){
                onBackwardListener.backward((float)valueY/100f);
            }
            if(valueX>(neutralizeState+tolerance)){
                steerRTListener.steerRT((float) (valueX/100f));
            }
            if(valueX<(neutralizeState-tolerance)){
                if(valueX<=0){
                    valueX=100f;
                }
                steerLTListener.steerLT((float)valueX/50f);
            }
            if((valueY>(neutralizeState-tolerance) && valueY<(neutralizeState+tolerance))
                    &&
                    (valueX>(neutralizeState-tolerance) && valueX<(neutralizeState+tolerance))){
                neutralizeListener.neutralize((float) valueX/100f, (float) (valueY/100f));
            }
            System.out.println("Vx : "+valueX);
            System.out.println("Vy : "+valueY);
        }, gpioPinAnalogInput);

        gpioPinDigitalInput.addListener((GpioPinListenerDigital) event -> {
            System.out.println("Vz = "+event.getState());
            onClickListener.onClick(Boolean.parseBoolean(String.valueOf(event.getState())));
        });

    }
    public interface OnForwardListener{
        void forward(float valueY);
    }
    public interface OnBackwardListener{
        void backward(float valueY);
    }
    public interface SteerRTListener {
        void steerRT(float valueX);
    }
    public interface SteerLTListener {
        void steerLT(float valueX);
    }
    public interface NeutralizeListener{
        void neutralize(float valueX,float valueY);
    }
    public interface OnClickListener{
        void onClick(boolean state);
    }
    public void setOnBackwardListener(OnBackwardListener onBackwardListener) {
        this.onBackwardListener = onBackwardListener;
    }

    public void setOnForwardListener(OnForwardListener onForwardListener) {
        this.onForwardListener = onForwardListener;
    }

    public void setSteerLTListener(SteerLTListener steerLTListener) {
        this.steerLTListener = steerLTListener;
    }

    public void setSteerRTListener(SteerRTListener steerRTListener) {
        this.steerRTListener = steerRTListener;
    }
    public void setNeutralizeListener(NeutralizeListener neutralizeListener){
        this.neutralizeListener=neutralizeListener;
    }
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
