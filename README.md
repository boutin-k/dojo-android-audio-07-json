# JSON

## But du TD
Dans le TD précédent, nous avons vu comment monter un fichier OBB dans notre application. L'archive OBB que nous avons chargé contient des chansons au format mp3 et un fichier JSON contenant des informations sur ces chansons.
Le fichier JSON est la base de donnée de notre OBB.
Nous allons parser le fichier JSON et stocker sont contenu dans une classe que nous nommerons JsonParser.

* Tu peux reprendre ton projet OBB ou alors tu peux récupérer une version [ici](https://github.com/WildCodeSchool/dojo-android-audio-obb).

## Etapes
### Créer une classe JsonParser
* Tu vas créer une classe *Singleton* que tu nommeras JsonParser.
* La classe JsonParser disposera d'une méthode prenant l'[InputStream](https://developer.android.com/reference/java/io/InputStream) du fichier JSON en paramètre définie comme tel:
```public void readJsonStream(InputStream inputStream)```
* Pour parser le contenu de l'InputStream, tu peux utiliser la classe [JsonReader](https://developer.android.com/reference/android/util/JsonReader) ou alors les classes [JSONObject](https://developer.android.com/reference/org/json/JSONObject) et [JSONArray](https://developer.android.com/reference/org/json/JSONArray).

> Comme InputStream est une classe qui implémente l'interface [Closeable](https://developer.android.com/reference/java/io/Closeable) nous pouvons utiliser la fonctionnalité [try-with-resources statement](https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html) pour nous assurer que le stream sera correctement fermé à la fin du traitement.

1. Si tu décides d'utiliser *JsonReader*, sache que cette classe implémente également l'interface *Closeable*.
2. Si tu décides d'utiliser *JSONObject/JSONArray*, voici le code qui transforme l'*InputSteam* en *String*:
```java
public void readJsonStream(InputStream in) {
  byte[] buffer = new byte[in.available()];
  int nbBytesRead = in.read(buffer);
  if (buffer.length == nbBytesRead) try {
    String stringify = new String(buffer);
  } catch (JSONException e) { e.printStackTrace(); }
}
```

### Stocker les données
* Tu vas stocker les données dans des variables membres à ton instance de classe JsonParser.
* Tu vas créer des accesseurs (getters) afin d'accéder à ces données en dehors de la classe.

### Toast
* Tu afficheras un Toast qui contiendra le texte ```Json read``` si le parse s'est bien passé.
* Tu afficheras un Toast qui contiendra le texte ```Json error``` si le parse s'est mal passé (throws Exception).

## Documentation
* [InputStream](https://developer.android.com/reference/java/io/InputStream)
* [JsonReader](https://developer.android.com/reference/android/util/JsonReader)
* [JSONObject](https://developer.android.com/reference/org/json/JSONObject)
* [JSONArray](https://developer.android.com/reference/org/json/JSONArray)
* [Closeable](https://developer.android.com/reference/java/io/Closeable)
* [try-with-resources statement](https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html)

## TD suivant
* [Recyclerview](https://github.com/boutin-k/dojo-android-audio-08-recyclerview)
