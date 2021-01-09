# JoyStickModule

[![](https://jitpack.io/v/Scrappers-glitch/JoystickModule.svg)](https://jitpack.io/#Scrappers-glitch/JoystickModule)

## In order to use the Library : ##

```gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

```gradle
dependencies {
	        implementation 'com.github.Scrappers-glitch:JoystickModule:1.0.1R'
	}
```

## Description :
Since , RPIs got no analog I/O pins , you cannot get direct analog mappings using the PI , so in order to read analog mappings ```0~1023``` as a hardware mapping or ```0%~100%``` as a pulse duty cycle , you will need to convert analog outputs to digital inputs through SPI (Serial peripheral Interface) of the PI

In order to find the wiringPI pins(wPi column on right & Left for RT & LT pins) on your device :

```
pi@raspberrypi:~ $ gpio readall
 +-----+-----+---------+------+---+---Pi 4B--+---+------+---------+-----+-----+
 | BCM | wPi |   Name  | Mode | V | Physical | V | Mode | Name    | wPi | BCM |
 +-----+-----+---------+------+---+----++----+---+------+---------+-----+-----+
 |     |     |    3.3v |      |   |  1 || 2  |   |      | 5v      |     |     |
 |   2 |   8 |   SDA.1 |   IN | 1 |  3 || 4  |   |      | 5v      |     |     |
 |   3 |   9 |   SCL.1 |   IN | 1 |  5 || 6  |   |      | 0v      |     |     |
 |   4 |   7 | GPIO. 7 |   IN | 1 |  7 || 8  | 1 | IN   | TxD     | 15  | 14  |
 |     |     |      0v |      |   |  9 || 10 | 1 | IN   | RxD     | 16  | 15  |
 |  17 |   0 | GPIO. 0 |  OUT | 0 | 11 || 12 | 0 | IN   | GPIO. 1 | 1   | 18  |
 |  27 |   2 | GPIO. 2 |   IN | 0 | 13 || 14 |   |      | 0v      |     |     |
 |  22 |   3 | GPIO. 3 |   IN | 0 | 15 || 16 | 0 | IN   | GPIO. 4 | 4   | 23  |
 |     |     |    3.3v |      |   | 17 || 18 | 0 | IN   | GPIO. 5 | 5   | 24  |
 |  10 |  12 |    MOSI |   IN | 0 | 19 || 20 |   |      | 0v      |     |     |
 |   9 |  13 |    MISO |   IN | 0 | 21 || 22 | 0 | IN   | GPIO. 6 | 6   | 25  |
 |  11 |  14 |    SCLK |   IN | 0 | 23 || 24 | 1 | IN   | CE0     | 10  | 8   |
 |     |     |      0v |      |   | 25 || 26 | 1 | IN   | CE1     | 11  | 7   |
 |   0 |  30 |   SDA.0 |   IN | 1 | 27 || 28 | 1 | IN   | SCL.0   | 31  | 1   |
 |   5 |  21 | GPIO.21 |   IN | 1 | 29 || 30 |   |      | 0v      |     |     |
 |   6 |  22 | GPIO.22 |   IN | 1 | 31 || 32 | 0 | IN   | GPIO.26 | 26  | 12  |
 |  13 |  23 | GPIO.23 |   IN | 0 | 33 || 34 |   |      | 0v      |     |     |
 |  19 |  24 | GPIO.24 |   IN | 0 | 35 || 36 | 0 | IN   | GPIO.27 | 27  | 16  |
 |  26 |  25 | GPIO.25 |   IN | 0 | 37 || 38 | 0 | IN   | GPIO.28 | 28  | 20  |
 |     |     |      0v |      |   | 39 || 40 | 0 | IN   | GPIO.29 | 29  | 21  |
 +-----+-----+---------+------+---+----++----+---+------+---------+-----+-----+
 | BCM | wPi |   Name  | Mode | V | Physical | V | Mode | Name    | wPi | BCM |
 +-----+-----+---------+------+---+---Pi 4B--+---+------+---------+-----+-----+
```


## Attachments : 

### Wrining PI for RPI4b & RPI3b :

![](https://github.com/Scrappers-glitch/JoyStickModule/blob/master/Attachments/j8header-3b.png)

### JoyStick Module Mappings in Vx & Vy where Vx is the potential difference across the POTx(potentiometer X) & Vy is the potential difference across the POTy(potentiometer y) :

![](https://github.com/Scrappers-glitch/JoyStickModule/blob/master/Attachments/Joystick-Module-Analog-Output.png)

### Arduino JoyStick Module Datasheet (NB: examples are in Arduinos) : 

https://github.com/Scrappers-glitch/JoyStickModule/blob/master/Attachments/joystick_module.pdf

### MCP3008 Channel & Communication Pins :

![](https://github.com/Scrappers-glitch/JoyStickModule/blob/master/Attachments/mcp3008pins.png)

### MCP3008 MicroChip ADC(Analog~Digital Converter) datasheet :

https://github.com/Scrappers-glitch/JoyStickModule/blob/master/Attachments/MCP3008.pdf

### If you need to know more about MCP3008 at MicroChip : 

https://www.microchip.com/wwwproducts/en/en010530#additional-features

### So , Generally do like this : 

```
VDD(driving device) -> 3v3 source.
Vref(reference voltage) -> 3v3 source.
AGND(Analog ground for closed circuit during analogRead transmission) -> GND.
CLK -> SCLK pin of the SPI0 , GPIO14 or physical pin 23 of PI4.
DOUT(the converted digital out of the MCP3008) -> MISO0(Master-in slave-out) of the SPI0 , GPIO13 or physical pin 21 of PI4.
DIN(Digital input from the PI4, for MCP3008 channels configuration as PI4 digitalOut) -> MOSI0(Master-out slave-in) of the SPI0 , GPIO12 or physical pin 19.
CS/SHDN(Chip Select or Chip Enable : used to select which SPI to enable as PI4b has 4 SPIs) -> CE0(Chip enable 0) , GPIO10 or physical pin 24 to enable SPI0.
DGND(Digital ground for closing circuit during digitalOut transmission) -> GND.

```

### Then on the joystick module there are 5 pins : 

```
GND -> GND 
+5v -> 5v0 or 3v3 source (it's tested in both cases)
VRx -> CH0 or CH1 or CH2 or CH3 or CH4 or CH5 or CH6 or CH7 according to your code setup but it should differs from VRy
VRy -> CH0 or CH1 or CH2 or CH3 or CH4 or CH5 or CH6 or CH7 according to your code setup but it should differs from VRx
SW(switch button pin) -> GPIO7 or pin7 for digital input ( i think you can still use MCP3008 channels as digitalInputs too with provisionDigitalInput(Pin) code setup , try it & tell e if it works :-) ). 

```
### start writing your code , but first implement it through gradle , then : 

# [CodeExample using jme Vehicles]
```java

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.environment.EnvironmentCamera;
import com.jme3.environment.LightProbeFactory;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.LightProbe;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.*;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.CompareMode;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.pi4j.gpio.extension.mcp.MCP3008Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.spi.SpiChannel;

import java.io.IOException;

public class JmeGame extends SimpleApplication implements ActionListener {

    private BulletAppState bulletAppState;
    private VehicleControl vehicle;
    private final float brakeForce = 300f;
    private final Vector3f jumpForce = new Vector3f(0, 2000, 0);
    private final Vector3f rushForce = Vector3f.UNIT_XYZ;
    private Spatial chassis;
    private final float accelerationForce = 2000.0f;
    private float steeringValue = 0;
    private float accelerationValue = 0;

    public static void main(String[] args) {
        JmeGame jmeGame=new JmeGame();
        jmeGame.start();
    }

    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.setDebugEnabled(false);
        addSky();
        createPhysicsTestWorld(rootNode, getAssetManager(), bulletAppState.getPhysicsSpace());
        buildPlayer();
        setupKeys();
//        addEnvLightProbe();
    }

    private void setupKeys() {
        JoystickModule joystickModule=new JoystickModule(SpiChannel.CS0);

        try {
            joystickModule.initializeModule(100, RaspiPin.GPIO_21, PinPullResistance.PULL_DOWN);
        } catch (IOException e) {
            e.printStackTrace();
        }
        joystickModule.registerChannel(JoystickModule.Channels.CH_0);
        joystickModule.registerChannel(JoystickModule.Channels.CH_1);
        joystickModule.registerVx(JoystickModule.Channels.CH_1);
        joystickModule.registerVy(JoystickModule.Channels.CH_0);
        joystickModule.setOnForwardListener(valueY -> {
            valueY+=accelerationForce;
            vehicle.accelerate(valueY);
        });
        joystickModule.setOnBackwardListener(valueY -> {
            valueY+=accelerationForce;
            vehicle.accelerate(-valueY);
        });
        joystickModule.setSteerRTListener(valueX -> vehicle.steer(-valueX/80f));
        joystickModule.setSteerLTListener(valueX -> vehicle.steer(valueX/20f));
        joystickModule.setNeutralizeListener((valueX, valueY) -> {
            vehicle.clearForces();
            vehicle.brake(brakeForce);
            vehicle.accelerate(0);
            vehicle.steer(0);
        });
        joystickModule.setOnClickListener(state -> vehicle.applyImpulse(jumpForce, Vector3f.ZERO));
        joystickModule.startCollectingChannelsData();

    }
    @Override
    public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("Lefts")) {
            if (value) {
                steeringValue += .5f;
            } else {
                steeringValue += -.5f;
            }
            vehicle.steer(steeringValue);
        } else if (binding.equals("Rights")) {
            if (value) {
                steeringValue += -.5f;
            } else {
                steeringValue += .5f;
            }
            vehicle.steer(steeringValue);
        } else if (binding.equals("Ups")) {
            if (value) {
                accelerationValue += accelerationForce;
            } else {
                accelerationValue -= accelerationForce;
            }
            vehicle.accelerate(accelerationValue);
        } else if (binding.equals("Downs")) {
            if (value) {
                vehicle.brake(brakeForce);
            } else {
                vehicle.brake(0f);
            }
        } else if (binding.equals("Space")) {
            if (value) {
                vehicle.applyImpulse(jumpForce, Vector3f.ZERO);
            }
        } else if (binding.equals("Reset")) {
            if (value) {
                System.out.println("Reset");
                vehicle.setPhysicsLocation(Vector3f.ZERO);
                vehicle.setPhysicsRotation(new Matrix3f());
                vehicle.setLinearVelocity(Vector3f.ZERO);
                vehicle.setAngularVelocity(Vector3f.ZERO);
                vehicle.resetSuspension();
            } else {
            }
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        /* deInitializeSensor Service on app exit , to be used with other apps */
    }

    private void addSky() {
        Geometry sky = (Geometry) SkyFactory.createSky(assetManager,assetManager.loadTexture("RocketLeauge/assets/Textures/sky.jpg"),Vector3f.UNIT_XYZ, SkyFactory.EnvMapType.EquirectMap);
        sky.getMaterial().getAdditionalRenderState().setDepthFunc(RenderState.TestFunction.LessOrEqual);
        getRootNode().attachChild(sky);
    }

    private PhysicsSpace getPhysicsSpace(){
        return bulletAppState.getPhysicsSpace();
    }

    /**
     * creates a simple physics test world with a floor, an obstacle and some test boxes
     *
     * @param rootNode where lights and geometries should be added
     * @param assetManager for loading assets
     * @param space where collision objects should be added
     */
    public  void createPhysicsTestWorld(Node rootNode, AssetManager assetManager, PhysicsSpace space) {
        AmbientLight a=new AmbientLight();
        a.setColor(new ColorRGBA(0.6f, 0.7f, 0.7f, 0.2f).mult(2));


        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        material.setTexture("ColorMap", assetManager.loadTexture("RocketLeauge/assets/Textures/soccerTex.jpg"));

        Material soccerPlayGround = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

//        soccerPlayGround.setTexture("EnvMap",assetManager.loadTexture("RocketLeauge/assets/Textures/sky.jpg"));
//        soccerPlayGround.selectTechnique("PostShadow",getRenderManager());

        soccerPlayGround.setTexture("ColorMap", assetManager.loadTexture("RocketLeauge/assets/Textures/metalBareTex.jpg"));



        floorGeometry=assetManager.loadModel("RocketLeauge/assets/Scenes/SoccerPlayGround.j3o");
        floorGeometry.setMaterial(soccerPlayGround);
//        DirectionalLight directionalLight=new DirectionalLight(new Vector3f(-3,-floorGeometry.getLocalScale().getY()*4,-3).normalize());
//        directionalLight.setColor(ColorRGBA.White.mult(2f));
        floorGeometry.addLight(a);
//        rootNode.addLight(directionalLight);

        floorGeometry.setLocalTranslation(0f,-10f,0f);
        floorGeometry.setLocalScale(15f,floorGeometry.getLocalScale().getY()*4,20f);
        floorGeometry.addControl(new RigidBodyControl(CollisionShapeFactory.createMeshShape(floorGeometry),0));
        rootNode.attachChild(floorGeometry);
        space.add(floorGeometry);

        //ball sphere with mesh collision shape
        Sphere sphere = new Sphere(15, 15, 5f);
        sphereGeometry = new Geometry("Sphere", sphere);
        sphereGeometry.setMaterial(createMat(ColorRGBA.White,"RocketLeauge/assets/Textures/soccerTex.jpg",sphereGeometry));
        sphereGeometry.setLocalTranslation(0f, -5f, 0f);
        sphereGeometry.setShadowMode(RenderQueue.ShadowMode.Cast);

        RigidBodyControl ballControl=new RigidBodyControl(new SphereCollisionShape(5f), 0.5f);
        ballControl.setFriction(2f);
        ballControl.setLinearVelocity(new Vector3f(0.2f,0.2f,0.2f));
        ballControl.setRollingFriction(1f);


        sphereGeometry.addControl(ballControl);
        rootNode.attachChild(sphereGeometry);
        space.add(sphereGeometry);

//        DirectionalLightShadowRenderer dlsr=new DirectionalLightShadowRenderer(assetManager,512,1);
//        dlsr.setLight(directionalLight);
//        dlsr.setShadowIntensity(0.2f);
//        dlsr.setLambda(0.55f);
//        dlsr.setShadowCompareMode(CompareMode.Hardware);
//        dlsr.setShadowZExtend(23f);
//        dlsr.setShadowZFadeLength(8f);
//        floorGeometry.setShadowMode(RenderQueue.ShadowMode.Receive);
//        viewPort.addProcessor(dlsr);


    }
    Spatial floorGeometry;
    Geometry sphereGeometry;


    ChaseCamera chaseCam;
    private void buildPlayer() {
        cam.setFrustumFar(2000f);
        Material mat = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(false);
        mat.setColor("Color", ColorRGBA.Black);

        //create a compound shape and attach the BoxCollisionShape for the car body at 0,1,0
        //this shifts the effective center of mass of the BoxCollisionShape to 0,-1,0
        CompoundCollisionShape compoundShape = new CompoundCollisionShape();
        BoxCollisionShape box = new BoxCollisionShape(new Vector3f(4.2f, 0.8f, 4.5f));

        compoundShape.addChildShape(box, new Vector3f(0, 2f, 0));

        chassis =assetManager.loadModel("RocketLeauge/assets/Models/ladaCar.j3o");
        chassis.setShadowMode(RenderQueue.ShadowMode.Cast);
        chassis.setLocalScale(2.2f,2.2f,2.2f);
        chassis.setLocalTranslation(new Vector3f(0, 1.2f, 0));
        //colors
        ((Node) chassis).getChild("glass").setMaterial(createMat(ColorRGBA.Black,"",null));
        ((Node) chassis).getChild("chassis").setMaterial(createMat(ColorRGBA.randomColor(), "",chassis));
        ((Node) chassis).getChild("addOns").setMaterial(createMat(null, "RocketLeauge/assets/Textures/bronzeCopperTex.jpg",null));
        ((Node) chassis).getChild("nitro").setMaterial(createMat(new ColorRGBA(0f,0f,5f,1f), "RocketLeauge/assets/Textures/metalBareTex.jpg",null));

        ((Node) chassis).getChild("frontLight").setMaterial(createMat(ColorRGBA.White,"",null));
        ((Node) chassis).getChild("backLights").setMaterial(createMat(ColorRGBA.Red,"",null));
        ((Node) chassis).getChild("uTurns").setMaterial(createMat(ColorRGBA.Yellow,"",chassis));
        ((Node) chassis).getChild("mirrors").setMaterial(createMat(ColorRGBA.White,"",null));


        //create vehicle node
        Node vehicleNode=new Node("vehicleNode");
        vehicleNode.attachChild(chassis);
        vehicleNode.setShadowMode(RenderQueue.ShadowMode.Cast);
//        chassis.setMaterial(mat);
        vehicle = new VehicleControl(compoundShape, 600f);
        vehicleNode.addControl(vehicle);
        vehicle.setPhysicsLocation(new Vector3f(20f,5f,10f));
        //add a chaseCam tomove the cam with the object

        chaseCam=new ChaseCamera(cam, vehicleNode);
        chaseCam.setDefaultDistance(-18f);
        chaseCam.registerWithInput(inputManager);
        chaseCam.setDragToRotate(true);
        //setting suspension values for wheels, this can be a bit tricky
        //see also https://docs.google.com/Doc?docid=0AXVUZ5xw6XpKZGNuZG56a3FfMzU0Z2NyZnF4Zmo&hl=en
        float stiffness =30.0f;//200=f1 car
        float compValue = 0.5f; //(should be lower than damp)
        float dampValue = 3f;
        //compression force of spring(Shock Producer)
        vehicle.setSuspensionCompression(compValue * 2.0f * FastMath.sqrt(stiffness));
        //stretch force of spring(Shock Absorber)
        vehicle.setSuspensionDamping(dampValue * 2.0f * FastMath.sqrt(stiffness));
        vehicle.setSuspensionStiffness(stiffness);
        vehicle.setMaxSuspensionForce(FastMath.pow(2, 20));

        //Create four wheels and add them at their locations
        Vector3f wheelDirection = new Vector3f(0,-1F, 0); // was 0, -1, 0
        Vector3f wheelAxle = new Vector3f(-6, 0, 0); // was -1, 0, 0
        float radius = 0.5f;
        float restLength = 0.1f;
        float yOff = radius;
        float xOff = 4*radius;
        float zOff = 6.5f*radius;

        Cylinder wheelMesh = new Cylinder(16, 16, radius, radius * 0.5f, true);

        Node node1 = new Node("wheel 1 node");
        Geometry wheels1 = new Geometry("wheel 1", wheelMesh);
        node1.attachChild(wheels1);
        wheels1.rotate(0, FastMath.HALF_PI, 0);
        wheels1.setMaterial(mat);
        vehicle.addWheel(node1, new Vector3f(-xOff, yOff, zOff),
                wheelDirection, wheelAxle, restLength, radius, true);

        Node node2 = new Node("wheel 2 node");
        Geometry wheels2 = new Geometry("wheel 2", wheelMesh);
        node2.attachChild(wheels2);
        wheels2.rotate(0, FastMath.HALF_PI, 0);
        wheels2.setMaterial(mat);
        vehicle.addWheel(node2, new Vector3f(xOff, yOff, zOff),
                wheelDirection, wheelAxle, restLength, radius, true);
        Node node3;
        Node node4;
        node3 = new Node("wheel 3 node");
        Geometry wheels3 = new Geometry("wheel 3", wheelMesh);
        node3.attachChild(wheels3);
        wheels3.rotate(0, FastMath.HALF_PI, 0);
        wheels3.setMaterial(mat);
        vehicle.addWheel(node3, new Vector3f(-xOff, yOff, -zOff),
                wheelDirection, wheelAxle, restLength, radius, false);

        node4 = new Node("wheel 4 node");
        Geometry wheels4 = new Geometry("wheel 4", wheelMesh);
        node4.attachChild(wheels4);
        wheels4.rotate(0, FastMath.HALF_PI, 0);
        wheels4.setMaterial(mat);
        vehicle.addWheel(node4, new Vector3f(xOff, yOff, -zOff),
                wheelDirection, wheelAxle, restLength, radius, false);

        vehicleNode.attachChild(node1);
        vehicleNode.attachChild(node2);
        vehicleNode.attachChild(node3);
        vehicleNode.attachChild(node4);
        rootNode.attachChild(vehicleNode);

        setWheelFrictionSlip(20f);

        getPhysicsSpace().add(vehicle);
        DirectionalLight directionalLight=new DirectionalLight(new Vector3f(2,2,2).mult(50).normalize());
        directionalLight.setColor(ColorRGBA.White);
        vehicleNode.addLight(directionalLight);





    }

    private void setWheelFrictionSlip(float frictionSlip) {
        for(int nOfWheel=0;nOfWheel<vehicle.getNumWheels();nOfWheel++) {
            vehicle.getWheel(nOfWheel).setFrictionSlip(frictionSlip);
        }
    }

    public Material createMat(ColorRGBA colorRGBA,String Tex,Spatial node){
        Material material=new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        /*metalness , max is 1*/
//        material.setFloat("Metallic", 0.5f);
//        /*Roughness , 1 is the max roughnesss*/
//        material.setFloat("Roughness", 0.5f);
//        material.setFloat("EmissivePower",1.0f);
//        material.setFloat("EmissiveIntensity",2.0f);
//        material.setBoolean("HorizonFade",true);
//        material.setVector3("LightDir",new Vector3f(-0.5f,-0.5f,-0.5f).normalize());
//        material.setBoolean("BackfaceShadows",true);

        if(colorRGBA !=null){
            /*Diffuse Color*/
            material.setColor("Color", colorRGBA);
            /*Reflection color*/
//            material.setColor("Specular", colorRGBA.mult(20f));
        }
        if(Tex.length() >1){
            Texture texture=assetManager.loadTexture(Tex);
            material.setTexture("ColorMap",texture);
        }
        material.setReceivesShadows(true);
        if(node !=null){
        }
        return material;
    }

    @Override
    public void simpleUpdate(float tpf) {

    }

}

```

### A photo for the Breadboard circuit :

![](https://github.com/Scrappers-glitch/JoyStickModule/blob/master/Attachments/IMG_20210109_145230.jpg)

### Video of Operation :

https://youtu.be/9ZvhFQSwHF0
