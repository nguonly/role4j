# Role4j
Role4j is a Java Framework for Role-oriented Programming. It is the successor of **LyRT**, also a Java-based role framework that heavily relies on reflection for method invocation. LyRT is a protypical implementation of [Dynamic Instance Binding](http://dx.doi.org/10.1145/2892664.2892687) for Run-time variability.

The improvements of this version are:
- More friendly by removing the method call by means of String. Instead, interfaces are used.
- Replacing the reflective method call to invokedynamic
- Aiming for overall performance by pre-fetching the method composition during each role operations such as binding, unbinding, transferring ,etc.

## Precaution
The current implementation of Role4j relies on sub-class proxying to intercept the method call and eventually facilitate the method invocation through InvokeDynamic. There are some shortcommings to keep in mind:
- The class definition of player, role and compartment cannot be `final`.
- Do not access property directly. Instead, get/set method shall be used.

## Declaration
Role4j is designed for highly modularity supporting not only at design time but also at run time.

There are 3 main essential interfaces namely `ICompartment`, `IPlayer` and `IRole`. In the upcoming release there will be `IRelationship`.

A player is declared with implementation of `IPlayer` interface.
``` java
public class Animal implements IPlayer{
  public void speak(){
    System.out.println("Animal speaks");
  }
}
```

A role is declared with implemtentation of `IRole` interface.
``` java
public class Duck implements IRole{
  public void speak(){
    System.out.println("Quark Quark");
  }
}

public class Bird implements IRole{
  public void speak(){
    System.out.println("Tweet Tweet");
  }
}
```

A compartment is declared with implementation of `ICompartment` interface.
``` java
public class AnimalWorld implements ICompartment{
  private String _name;
  
  public AnimalWorld(){}
  
  public AnimalWorld(String name){
    _name = name;
  }
}
```

## Binding at run time
``` java
public class AnimalWorldExample{
  public static void main(String... args) throws Throwable{
    Registry reg = Registry.getInstance(); //get reference to registry
    Animal animal = reg.newCore(Animal.class); //initialize a player
    AnimalWorld groundWorld = reg.newCompartment(AnimalWorld.class, "Ground"); //compartment
    AnimalWorld skyWorld = reg.newCompartment(AnimalWorld.class, "Sky");
    
    animal.speak(); //It should print Animal speaks
    groundWorld.activate(); //bring the compartment for binding or method call
    animal.bind(Duck.class); //being a duck now
    animal.speak(); //It should print Quark Quark
    groundWorld.deactivate(); //deactivate compartment. Binding still remains.
    
    skyworld.activate();
    animal.bind(Bird.cass); //being a bird now
    animal.speak(); //It should print Tweet Tweet
    skyWorld.deactivate();
    
    animal.speak(); //It should go to original behavior which is Animal speaks
  }
}
```

## Object Morphing
Once a core object plays role, it inherits all the properties and behaviors of those bound roles. Consider an `Any.java` with empty body that it can become `Human` or `Animal`.

``` java
public class Any implements IPlayer{
}

public class Animal implement IRole{
  public void speak(){
    System.out.println("Animal speaks");
  }
}

public class Human implements IRole{
  public void speak(){
    System.out.println("Human speaks");
  }
}

public class World implements ICompartment{
}

public class ObjectMorphingExample{
  public static void main(String... args) throws Throwable{
    Registry reg = Registry.getInstance();
    Any any = reg.newCore(Any.class);
    World world = reg.newCompartment(World.class);
    
    world.activate();
    any.bind(Animal.class);
    any.interfaceOf(Animal.class).speak(); //Animal speaks
    
    any.unbind(Animal.class); //no longer play Animal role
    
    any.bind(Human.class); //being a human 
    any.interfaceOf(Human.class).speak(); //Human speaks
    world.deactivate();
  }
}
```

## Roles play roles
Roles can also being players that can other roles. Below is an example of networking containing `Sender` as a player that might play `Encryption` or `Compression` role or both.

``` java
public class Sender implements IPlayer, ISending{
  public String send(){
    return "data";
  }
}

//An interface to to hook up the proceed method style in Context-oriented Programming.=
interface ISending{
  String send();
}

public class Encryption implements IRole, ISending{
  public String send(){
    String proceed = getPlayer(ISending.class).send();
    return "<E>" + proceed + "<E>";
  }
}

public class Compression implements IRole, ISending{
  public String send(){
    String proceed = getPlayer(ISending.class).send();
    return "<C>" + proceed + "<C>";
  }
}

public class Networking implements ICompartment{
}

public class NetworkingExample{
  public static void main(String... args) throws Throwable{
    Registry reg = Registry.getInstance();
    Sender sender = reg.newCore(Sender.class);
    
    Networking securedEnv = reg.newCompartment(Networking.class);
    Networking lowBandwidthEnv = reg.newCompartment(Networking.class);
    Networking bothEnv = reg.newCompartment(Networking.class);
    
    securedEven.activate();
    sender.bind(Encryption.class);
    securedEven.deactivate();
    
    lowBandwidthEnv.activate();
    sender.bind(Compression.class);
    lowBandwidthEnv.deactivate();
    
    bothEnv.activate();
    sender.bind(Encryption.class).bind(Compression.class); // role plays role
    bothEnv.deactivate();
    
    System.out.println(sender.send()); // data
    
    securedEnv.activate();
    System.out.println(sender.send()); // <E>data<E>
    securedEvn.deactivate();
    
    lowBandwidthEnv.activate();
    System.out.println(sender.send()); // <C>data<C>
    lowBandwidthEnv.deactivate();
    
    bothEnv.activate();
    System.out.println(sender.send()); // <C><E>data<E><C>
    bothEnv.deactivate();
  }
}
```

## Implementation

### Lookup Table Structure

- **Compartment**:  Object refers to compartment instance
- **CompartmentType**: Class of the compartment type
- **ProxyCompartment**: Proxy compartment instance for method interceptor
- **Object**: a core object instance
- **ObjectType**: class type of core object instance
- **ProxyObject**: proxy core object for method interceptor
- **Player**: a player instance
- **PlayerType**: List of classes (types) referencing to a player type, its super classes and its interfaces
- **ProxyPlayer**: a proxy player for method interceptor
- **Role**: a role instance
- **RoleType**: a type of role
- **ProxyRole**: a proxy role instance for method interceptor
- **Level**: represents a depth level of binding with regard to core object
- **Sequence**: represents the ordering number of a given level
