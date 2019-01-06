# DB Data generator

## Build

`$ ./gradlew fatJar`

## Run

`$ java -jar com.lamtev.movie-service.datagen-1.0.RELEASE.jar <path to DB config file> <path to params file>`

Where DB config file is json of the following form

```
{
    "url": "url_to_postgres",
    "user": "user",
    "password": "password"
}
```

and params file looks like [params.json](http://gitlab.icc.spbstu.ru/anton/movie-service/blob/master/lab3/data-generator/params.json).

