# lib-patterns

Team33 Patterns Library Collection

## Home

http://www.team33.de/dev/patterns/

## Artifacts

* patterns-bom (Team33 Patterns Library BOM)
* patterns-lib (Team33 Patterns Library)
  * testing-01 (Team33 Testing Library - Edition 1) - **deprecated**
  * testing-titan (Team33 Testing Library - Edition "titan")
  * production-01 (Team33 Production Library - Edition 1)
  * random-01 (Team33 Randomization Library - Edition 1) - **deprecated**
  * random-mimas (Team33 Randomization Library - Edition "mimas") - **deprecated**
  * random-tarvos (Team33 Randomization Library - Edition "tarvos")
  * exceptional-01 (Team33 Exceptional Library - Edition 1)
  * lazy-01 (Team33 Lazy Initialization Library - Edition 1) - **deprecated**
  * lazy-narvi (Team33 Lazy Initialization Library - Edition "narvi")
  * pooling-01 (Team33 Pooling Library - Edition 1)
  * tuple-janus (Team33 Tuple Library - Edition "janus")
  * reflect-luna (Team33 Reflection Library - Edition "luna")
  * expiry-tethys (Team33 Expiration Library - Edition "tethys")
  * properties-01 (Team33 Properties Library - Edition 1)
  * building-elara (Team33 Building Library - Edition "elara")

## Change Log

### 1.11.1

* reflect-luna ...
  * de.team33.patterns.reflect.luna.Fields
    * added method stream()
    * added method toMap()
    * refined samples

### 1.11.0

* Refactored project structure


    patterns-parent              patterns-root
    +-- exceptional-01           +-- patterns-bom
    +-- ...                      +-- patterns-lib
    :                                +-- exceptional-01
    +-- building-elara               :
    +-- patterns-bom                 +-- building-elara

### 1.10.1

* building-elara
  * Refined existing classes
  * Added several classes and funktionality

### 1.10.0

* Added module building-elara

