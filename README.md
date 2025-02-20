# lib-patterns

Team33 Patterns Library Collection

## Home

http://www.team33.de/dev/patterns/1.x

## Requires

    Java 8 or later

## Artifacts

* patterns-bom (Team33 Patterns Library BOM)
* patterns-lib (Team33 Patterns Library)
  * arbitrary-mimas (Team33 Arbitrary Library - Edition "mimas")
  * building-elara (Team33 Building Library - Edition "elara")
  * collection-ceres (Team33 Collection Library - Edition "ceres")
  * decision-carpo (Team33 Decision Library - Edition "carpo")
  * enums-pan (Team33 Enums Library - Edition "pan")
  * exceptional-dione (Team33 Exceptional Library - Edition "dione")
  * execution-metis (Team33 Execution Library - Edition "metis")
  * expiry-tethys (Team33 Expiration Library - Edition "tethys")
  * hashing-pandia (Team33 Hashing Library - Edition "pandia")
  * io-deimos (Team33 IO Library - Edition "deimos")
  * io-phobos (Team33 IO Library - Edition "phobos")
  * lazy-narvi (Team33 Lazy Initialization Library - Edition "narvi")
  * notes-eris (Team33 Notifications Library - Edition "eris")
  * pooling-ariel (Team33 Pooling Library - Edition "ariel")
  * reflect-luna (Team33 Reflection Library - Edition "luna")
  * reflect-pandora (Team33 Reflection Library - Edition "pandora")

## Change Log

### 1.24.1

* Nailed module serial-charon to 1.24.0 (discontinued)
* Nailed module tuple-janus to 1.24.0 (discontinued)

### 1.24.0

* Nailed module decision-leda to 1.23.0 (discontinued)
* Added module decision-carpo

### 1.23.0

* Nailed module decision-telesto to 1.22.0 (discontinued)
* Added module decision-leda

### 1.22.0

* Added module hashing-pandia

### 1.21.1

* Refined module enums-pan ...
  * Added method Values.mapAll(Predicate, Function)
  * Refined javadoc
* Refined module exceptional-dione ...
  * Added class Ignoring
  * Refined testing

### 1.21.0

* Refined module enums-pan ...
  * Added class Values
  * Marked class EnumValues as deprecated
* Refactored module io-phobos

### 1.20.0

* Added module enums-pan

### 1.19.3

* Nailed deprecated module testing-titan to 1.19.2

### 1.19.2

* Marked module testing-titan as deprecated

### 1.19.1

* Refined module arbitrary-mimas
* Nailed deprecated module random-tarvos to 1.19.0

### 1.19.0

* Added module arbitrary-mimas
* Marked module random-tarvos as deprecated

### 1.18.3

* Refined module io-deimos ...
  * Added class Resource
  * Marked TextIO.read(InputStream) as deprecated

### 1.18.1

* building-elara
  * Refined existing classes
  * Added class DataBuilder

### 1.18.0

* Added module collection-ceres
* Removed some deprecated methods from non-deprecated classes
  * de.team33.patterns.building.elara.Charger.release()
  * de.team33.patterns.exceptional.dione.Revision.finish()
  * de.team33.patterns.exceptional.dione.Revision.finish(Function)

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

