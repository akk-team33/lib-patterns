# lib-patterns

Team33 Patterns Library Collection

## Home

http://www.team33.de/dev/patterns/1.x

## Requires

    Java 8 or later

## Artifacts

* patterns-bom (Team33 Patterns Library BOM)
* patterns-lib (Team33 Patterns Library)
  * exceptional-dione (Team33 Exceptional Library - Edition "Dione")
  * random-tarvos (Team33 Randomization Library - Edition "Tarvos")
  * io-deimos (Team33 IO Library - Edition "Deimos")
  * io-phobos (Team33 IO Library - Edition "Phobos")
  * lazy-narvi (Team33 Lazy Initialization Library - Edition "Narvi")
  * pooling-ariel (Team33 Pooling Library - Edition "Ariel")
  * expiry-tethys (Team33 Expiration Library - Edition "Tethys")
  * tuple-janus (Team33 Tuple Library - Edition "Janus")
  * reflect-luna (Team33 Reflection Library - Edition "Luna")
  * reflect-pandora (Team33 Reflection Library - Edition "Pandora")
  * building-elara (Team33 Building Library - Edition "Elara")
  * notes-eris (Team33 Notifications Library - Edition "Eris")
  * execution-metis (Team33 Execution Library - Edition "Metis")
  * serial-charon (Team33 Serial Collection Library - Edition "Charon")
  * decision-telesto (Team33 Decision Library - Edition "Telesto")
  * normal-iocaste (Team33 Normalization Library - Edition "Iocaste")
  * testing-titan (Team33 Testing Library - Edition "Titan")

## Change Log

### next

* Added module normal-iocaste

### 1.17.0

* Added module decision-telesto
* Added module io-phobos

### 1.16.0

* Added module reflect-pandora

### 1.15.0

* Added module io-deimos ...
  * Added class TextIO
* Refined module testing-titan ...
  * Marked class TextIO as deprecated

### 1.14.2

* Refined module testing-titan ...
  * Added class FileIO
  * Added class ZipIO
  * Added class TextIO
  * Added class FileInfo
* Refined module expiry-tethys ...
  * Added class XRecent
* Refined module pooling-ariel ...
  * Added class XRProvider

### 1.14.1

* Added class Redirected to module testing-titan

### 1.14.0

* Added module serial-charon

### 1.13.2

* Nailed deprecated module exceptional-01 to 1.13.1
* Prepared site url for multiple major releases

### 1.13.1

* Nailed deprecated module exceptional-01 to 1.13.0
* Nailed deprecated module production-01 to 1.13.0
* Nailed deprecated module random-mimas to 1.13.0
* Nailed deprecated module lazy-01 to 1.13.0
* Nailed deprecated module properties-01 to 1.13.0
* Added module pooling-ariel
  * Marked module pooling-01 as deprecated

### 1.13.0

* Marked module properties-01 as deprecated
* Marked module production-01 as deprecated
* Nailed deprecated module random-01 to 1.12.2
* Nailed deprecated module testing-01 to 1.12.2
* Added module exceptional-dione
  * Marked module exceptional-01 as deprecated
* Refined module random-tarvos ...
  * added simple factory methods to Generator

### 1.12.2

* Added class RProvider to module pooling-01
* Refined module expiry-tethys
* Refined module reflect-luna ...
  * Added option <public fields> to class Fields
  * Fields ignorable by name

### 1.12.0

* Added module notes-eris
* Added module execution-metis

### 1.11.1

* reflect-luna ...
  * de.team33.patterns.reflect.luna.Fields
    * added method stream()
    * added method toMap()
    * refined samples

### 1.11.0

* Refactored project structure

### 1.10.1

* building-elara
  * Refined existing classes
  * Added several classes and functionality

### 1.10.0

* Added module building-elara

