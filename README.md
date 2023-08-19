# lib-patterns

Team33 Patterns Library Collection

## Home

http://www.team33.de/dev/patterns/

## Requires

    Java 8 or later

## Artifacts

* patterns-bom (Team33 Patterns Library BOM)
* patterns-lib (Team33 Patterns Library)
  * exceptional-dione (Team33 Exceptional Library - Edition "dione")
  * random-tarvos (Team33 Randomization Library - Edition "tarvos")
  * lazy-narvi (Team33 Lazy Initialization Library - Edition "narvi")
  * pooling-01 (Team33 Pooling Library - Edition 1)
  * expiry-tethys (Team33 Expiration Library - Edition "tethys")
  * tuple-janus (Team33 Tuple Library - Edition "janus")
  * reflect-luna (Team33 Reflection Library - Edition "luna")
  * building-elara (Team33 Building Library - Edition "elara")
  * notes-eris (Team33 Notifications Library - Edition "eris")
  * execution-metis (Team33 Execution Library - Edition "metis")
  * testing-titan (Team33 Testing Library - Edition "titan")
* deprecated (declared)
  * exceptional-01 (Team33 Exceptional Library - Edition 1)
  * production-01 (Team33 Production Library - Edition 1)
  * random-mimas (Team33 Randomization Library - Edition "mimas")
  * lazy-01 (Team33 Lazy Initialization Library - Edition 1)
  * properties-01 (Team33 Properties Library - Edition 1)
* deprecated (nailed)
  * random-01 (Team33 Randomization Library - Edition 1) - 1.12.2
  * testing-01 (Team33 Testing Library - Edition 1) - 1.12.2

## Change Log

### next

* ...

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
  * Added several classes and funktionality

### 1.10.0

* Added module building-elara

