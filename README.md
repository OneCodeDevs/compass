# Navigator

This navigator library offers a KSP API to generate needed boiler plate code for Jetpack Compose Navigation, when going full Compose with your app.

## API

The API consists of five Annotations

##### @Destination
The @Destination annotation defines and generates a composable destination in the Compose NavHost. The generated function is a a placeholder where Composables can be called from to create a screen.

```Kotlin
@Destination
object SimpleUi
```

generates

```Kotlin
Navigator {
  simpleUiScreen {
    // add your Composables here
  }
}
```

#### @Navigation
With @Navigation routes to another destination can be added. A destination can define as many navigation routes as it wants by simple adding multiple @Navigation annotations. The target of a navigation has to be annotated with @Desination as well.

```Kotlin
@Destination
@Navigation(to = Second::class)
object First

@Destination
object Second
```

generates

```Kotlin
Navigator {
  firstScreen {
    navigateToSecond()  // generated
  }
  secondScreen {
    // add your Composables here
  }
}
```

#### @Parameter
A destination can define parameters it wants to receive when navigated to. The defined parameters will be added to any generated navigation function, targeting the destination.

Currently supported parameter values are:
- Numbers, like Int, Long, Double, Float, etc.
- String

```Kotlin
@Destination
@Navigation(to = Second::class)
object First

@Destination
@Parameter(name = "someNumber", type = Int::class)
object Second
```

generates

```Kotlin
Navigator {
  firstScreen {
    navigateToSecond(someNumber = 10)  // generated with the specified parameter of destination Second
  }
  secondScreen {
    // add your Composables here
  }
}
```

#### @Home
@Home marks a destination as the start destination of the navigation graph or the subgraph. It will be the first destination to be in this graph.

#### @SubGraph
In addition to a full graph which will generate the **Navigator**, destinations can also be grouped together into a subgraph. The subgraph can be defined independently and later included into an existing **Navigator**. This is e.g. useful in multi module projects or for defining something like a wizard.

```Kotlin
@Destination
@Home
@Navigation(to = Second::class)
object FirstSub

@Destination
@Parameter(name = "someNumber", type = Int::class)
object SecondSub

@SubGraph(
  name = "Wizard",
  FirstSub::class,
  SecondSub::class
)
object Module

```

generates

```Kotlin
wizardSubGraph {
  firstSubScreen {
      navigateToSecond(someNumber = 10)  // generated with the specified parameter of destination SecondSub
    }
    secondSubScreen {
      // add your Composables here
    }
}
```

**wizardSubGraph** is an extension function of **NavGraphBuilder** and can therefore be directly embedded into the **Navigator** lambda.

Annotated destinations can be separate across the app or put together in a single file.
Valid targets of all annotations are `object`, `class` or `interface`.

## Example
A detailed example can be found in the [demo app](demo/app/src/main/java/io/redandroid/navigator/demo) for putting a **Navigator** together
and [demo library](demo/wizard/src/main/java/io/redandroid/navigator/demo/wizard) for configuring a sub graph
