# Grogu

Grogu is an android app that is designed to find Star Wars characters and their details. It uses [swapi API](https://swapi.dev/) to load the data into the app.

Single activity, Kotlin-powered reliable Star Wars app.

* Type the name of the character
* See results in the list
* ✨Magic ✨

# Features

Full character name-based search

* Shows full movie details of the searched character.
* Shows information like Height, Birth Year.
* Shows species information like Planet name and Language.

> Supports devices with Android Version > **20 (Lollipop)** and targets the Android Version **32 (R)**

# Assumptions

* Because a lot of new features can be added to the app in the future, adding those features or changes should be performed easily. 
* Data source(s) might get modified in the future and the app design/architecture should be able to adapt to it effortlessly.
* Code should be readable to make collaboration within the team effortless.
* Grogu is made while keeping simplicity a priority.

## Before Writing the Code

### Why MVVM, not MVI?

Since MVI is a great option when a lot of user input is involved in the app user interface, the current required features are not heavily user-driven, so driving the presentation layer architecture around MVI would be an overkill. Here only one input from the user, that is, making a search is part of the user interface design, hence MVVM architecture is chosen.

### Why Clean Architecture?

Clean Architecture allows better control over the dependencies flow in an app which leads to a better testable, flexible, and easier-to-follow codebase. As mentioned in the assumptions, new features should be extremely easy to accommodate and the scalability of the app in the future must be immensely adaptable. For example, redesigning app UI or modifying the data source(s) should be a piece of cake, as should be adding a new business requirement.

The third-party libraries needed to build the app had to be identified before starting to write the code.

| **Purpose**                       | **Library Used**  | **Why this Library?**                                                |
| ----------------------------------| ----------------- | ---------------------------------------------------------------------|
| Dependency Injection              | Hilt              | Easy to use DI containers with automatic lifecycle event management  |
| Networking                        | okhttp3           | Easy to use HTTP library                                             |
|                                   | Retrofit          | Easy to use type-safe HTTP client                                    |
| JSON Serialization/Deserialization| Gson              | Works well with Retrofit for handling request & response body        |
| Handling Paginated Data           | Pagination 3      | Provides really efficient paging implementation                      |
| User Experience                   | Facebook Shimmer  | Provides an easy way to add a shimmer effect                         |
| Concurrency                       | Coroutines        | Makes it very easy to do asynchronous programming                    |
| Testing                           | Mockito           | Provides easy API to create mocks of objects during testing          |
|                                   | Espresso          | Simple & popular UI testing library                                  |
|                                   | CashApp Turbine   | Very simple library to test Kotlin Flows                             |
|                                   | JUnit             | Best Java unit testing framework                                     |

## Project Structure

* **Domain**: This is the domain layer. It is responsible for defining all the entities and APIs used by Presentation and the Data layer.

* **Data**: This is the data layer. It is responsible for implementing Domain APIs and interacting with data. It defines its own entities which it uses to interact with the outside world (i.e remote server).

* **Presentation**: This is the _presentation layer_. It is responsible for handling all the UI-related logic and activities/fragments that users can use and interact with.

There are a few more directories:

* **DI**: This is where Hilt modules reside, connected to the different layers of the application.

* **Utils**: Classes and functions which are used in the corresponding layer.

* **Extensions**: Extension functions on the objects which are used in the corresponding layer.


# Implementation

<p align="center"><img src="/imgs/high_level_dig.png" width="350"/></p>


### **Domain**

Repository and the respective entities were initially created as the domain layer defines all the core business logic and APIs.

### **Data**

In the next step, Retrofit had to be set up because it would be the first dependency that will be needed to inject via Hilt.


StarWarsAPI interface was created alongside its data layer entities to make all the required HTTP calls. These data layer entities were also annotated with their respective JSON keys. 
e.g. @SerializedName("title") for JSON serialization/deserialization. 

```kotlin
data class FilmEntity (

    @SerializedName("url")
    val url: String,

    @SerializedName("opening_crawl")
    val openingCrawl: String,

    @SerializedName("title")
    val title: String,
)
```

To keep the dependencies only one way, an extension function (i.e. toDomainModel) was added to all the data layer entities which mapped Data entities to Domain entities. By doing this, data layer entities are going to be inside the data layer only.

```kotlin
fun FilmEntity.toDomainModel(): Film {
    return Film(url, openingCrawl, title)
}
```

An implementation of StarWarsRepository needed to be injected into ViewModels, hence its implementation, that is, StarWarsRepositoryImpl was created. Then the Hilt module, that is, DataModule, was developed accordingly to provide StarWarsRepositoryImpl at the runtime. 

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

   ... 

    @Provides
    @Singleton
    fun provideStarWarsRepository(retrofit: Retrofit): StarWarsRepository {
        return StarWarsRepositoryImpl(retrofit.create(StarWarsAPI::class.java))
    }
}
```

### **Presentation**

The paging source, that is, CharacterSearchPagingSource, for the character search was created and called up in the HomeViewModel. The layout of HomeFragment was designed by using RecyclerView and EditText. Then, DataBinding was used to bind the HomeFragment with the data which was served to it by HomeViewModel. 

<p align="center"><img src="/imgs/high_level_dia_presentation.png" width="350"/></p>

<p align="center"><img src="/imgs/screenshot_home.png" width="250"/>      <img src="/imgs/screenshot_home_2.png" width="250"/></p>

To reduce the complexity of the DetailFragment layout, multiple parts of its layout were separately created and then included in the DetailFragment layout. Similar to above, DataBinding was used to bind the DetailFragment with the data which was served to it by DetailViewModel.

<p align="center"><img src="/imgs/screenshot_detail.png" width="250"/></p>

While creating the UI, a few functions were required to make the data coming from the API standard and more readable (e.g., the population returned by the API was formatted like: 20000, but the desired output was to make it look like: 20,000). Here, all the Domain Entities came to the rescue, and all those functions were added to those entities (e.g. formatHeightInCm, getFormattedPopulation, etc.) 

```kotlin
     /**
     * Returns the [height] with cm suffix
     *
     * e.g. 172 cm
     */
    fun formatHeightInCm(): String {
        val intHeight = height.toIntOrNull() ?: return height.capitalizeFirstChar()
        return "$intHeight cm"
    }
```

### **Testing**

**Unit Testing**

To start simple with tests, they were built for all the domain entities' functions. Later, all the edge cases of ViewModel were tested using JUnit, and Mockito was used to mock all the required API responses and the function calls. 

**UI Testing**

A fake repository, that is, FakeStarWarsRepository, was added to keep the test running quickly. Espresso was used to verify all the navigations of the app, and correct data population happening on the user interface. 
