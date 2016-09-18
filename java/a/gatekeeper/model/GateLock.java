package a.gatekeeper.model;

import java.util.Date;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.*;

public class GateLock implements GpioPinListenerDigital
{
  public void
  handleGpioPinDigitalStateChangeEvent( GpioPinDigitalStateChangeEvent evt )
  {
    System.out.println( " --> GPIO PIN STATE CHANGE: " +
                        evt.getPin() +
                        " = " +
                        evt.getState() );
  }

  public GateLock()
  {
    gpio_ = GpioFactory.getInstance();

    // IMPORTANT match GPIO_nn constant with correct pin

    pin_ = gpio_.provisionDigitalOutputPin( RaspiPin.GPIO_17,
                                            "LOCK",
                                            PinState.LOW );

    pin_.addListener( this );

    pin_.setShutdownOptions( true,
                             PinState.LOW,
                             PinPullResistance.OFF );
  }

  public void pulseOpen()
  {
    pin_.pulse( DURATION_MSEC );
  }

  public static final long DURATION_MSEC = 5000L; // 5 sec

  private GpioController gpio_;
  private GpioPinDigitalOutput pin_;
}
