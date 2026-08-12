# lib-patterns

Team33 Patterns Library Collection

## Home

https://www.team33.de/dev/patterns/2.x

## Requires

Java 17 or later

## Artifacts

* patterns-bom (Team33 Patterns Library BOM)
* patterns-lib (Team33 Patterns Library)
  * arbitrary-mimas (Team33 Arbitrary Library - Edition "mimas")
  * decision-thyone (Team33 Decision Library - Edition "thyone")
  * enums-pan (Team33 Enums Library - Edition "pan")
  * exceptional-dione (Team33 Exceptional Library - Edition "dione")
  * files-pluto (Team33 Files Library - Edition "pluto")
  * files-styx (Team33 Files Library - Edition "styx")
  * io-thalassa (Team33 IO Library - Edition "thalassa")
  * lazy-narvi (Team33 Lazy Initialization Library - Edition "narvi")
  * records-triton (Team33 Records Library - Edition "triton")
  * streamable-galatea (Team33 Streamable Library - Edition "galatea")

## Still Featured

* (patterns-lib - previous releases)
  * building-anthe (Team33 Building Library - Edition "anthe") - 2.8.0
  * building-elara (Team33 Building Library - Edition "elara") - 2.8.0
  * collection-ceres (Team33 Collection Library - Edition "ceres") - 2.8.0
  * execution-metis (Team33 Execution Library - Edition "metis") - 2.7.0
  * expiry-tethys (Team33 Expiration Library - Edition "tethys") - 2.7.2
  * hashing-pandia (Team33 Hashing Library - Edition "pandia") - 2.7.2
  * matching-rhea (Team33 Pattern Matching Library - Edition "rhea") - 2.7.0
  * notes-eris (Team33 Notifications Library - Edition "eris") - 1.25.0
  * pooling-ariel (Team33 Pooling Library - Edition "ariel") - 2.7.0
  * proving-kerberos (Team33 Proving Library - Edition "kerberos") - 2.7.0
  * reflect-luna (Team33 Reflection Library - Edition "luna") - 2.7.2
  * reflect-pandora (Team33 Reflection Library - Edition "pandora") - 2.7.2
  * tuple-janus (Team33 Tuple Library - Edition "janus") - 1.24.0

## Deprecated

* (patterns-lib - previous releases)
  * decision-carpo (Team33 Decision Library - Edition "carpo") - 2.6.1
  * decision-leda (Team33 Decision Library - Edition "leda") - 1.26.0
  * decision-telesto (Team33 Decision Library - Edition "telesto") - 1.26.0
  * hierarchy-mab (Team33 Hierarchy Library - Edition "mab") - 2.8.0
  * io-adrastea (Team33 IO Library - Edition "adrastea") - 2.8.0
  * io-deimos (Team33 IO Library - Edition "deimos") - 2.8.0
  * io-phobos (Team33 IO Library - Edition "phobos") - 2.8.0
  * random-mimas (Team33 Randomization Library - Edition "mimas") - 1.13.0
  * random-tarvos (Team33 Randomization Library - Edition "tarvos") - 1.19.0
  * serial-charon (Team33 Serial Collection Library - Edition "charon") - 1.27.0

## Change Log

### ☐ near future

* ☐ Module records-triton:
  * ☐ Support for 'stringable' types
  * ☐ Support for 'listable' types
  * ☐ Support for 'mapable' types

### 2.8.1

* Pinned module io-deimos to 2.8.0 (deprecated)
* Pinned module io-adrastea to 2.8.0 (deprecated)
* Pinned module hierarchy-mab to 2.8.0 (deprecated)
* Pinned module io-phobos to 2.8.0 (deprecated)
* Pinned module building-anthe to 2.8.0 (still featured)
* Pinned module building-elara to 2.8.0 (still featured)
* Pinned module collection-ceres to 2.8.0 (still featured)
* Module records-triton: added Options to JSON rendering
* Module files-styx:
  * Fixed basic ordering (IGNORE_CASE, RESPECT_CASE)
  * Added/refined javadoc

### 2.8.0

* Marked module io-deimos as deprecated
* Marked module io-adrastea as deprecated (refined)
* Marked module hierarchy-mab as deprecated (refined)
* Unpinned and marked module io-phobos as deprecated
* Pinned module hashing-pandia to 2.7.2 (still featured)
* Pinned module expiry-tethys to 2.7.2 (still featured)
* Pinned module reflect-luna to 2.7.2 (still featured)
* Pinned module reflect-pandora to 2.7.2 (still featured)

### 2.7.2

* Added module files-pluto
* Added module files-styx
* Marked module io-adrastea as deprecated
* Marked module hierarchy-mab as deprecated
* Refined module io-deimos ...
  * refined dependencies
  * fixed javadoc

### 2.7.1

* Added module io-thalassa
* Refined module io-deimos ...
  * Marked class TextIO as deprecated
* Pinned module execution-metis to 2.7.0 (still featured)
* Pinned module matching-rhea to 2.7.0 (still featured)
* Pinned module pooling-ariel to 2.7.0 (still featured)
* Pinned module proving-kerberos to 2.7.0 (still featured)

### 2.7.0

* Added module records-triton
* Pinned module decision-carpo to 2.6.1 (deprecated)

### 2.6.1

* Pinned module serial-charon to 1.27.0 (deprecated)
* Unpinned module decision-carpo
  * Marked module as deprecated

### 2.6.0

* Added module hierarchy-mab
* Added module io-adrastea
* Refined module decision-thyone ...
  * added methods Choices.applying(*)

### 2.5.0

* Added module decision-thyone
* Pinned module decision-carpo to 2.4.1 (still featured)

### 2.4.1

* Pinned module decision-leda to 1.26.0 (deprecated)
* Pinned module decision-telesto to 1.26.0 (deprecated)

### 2.4.0

* Added module proving-kerberos

### 2.3.0

* Added module streamable-galatea
* Refined module building-elara ...
  * added method Setup.forEach(...)
  * removed deprecated class BuilderBase

### 2.2.0

* Added module building-anthe
* Refined module building-elara ...
  * Marked class BuilderBase as deprecated

### 2.1.3

* Refined module lazy-narvi ...
  * removed deprecated items
  * ReLazy & XReLazy: refactored reset() to avoid unnecessary activity
* Refined module expiry-tethys ...
  * refactored implementation
  * thoroughly refactored tests to reduce fragility
* Some more slight refinements

### 2.1.2

* Using de.team33:mvn-config-alpha:2.0.0 as super parent pom
* Refined module decision-carpo ...
  * fixed: Cases may reply _null_

### 2.1.1

* Refined module lazy-narvy ...
  * Added method peek() to class LazyFeatures
  * Added javadoc to class LazyFeatures

### 2.1.0

* Unpinned and refined module collection-ceres
  * Added method Collecting.retain(Collection, Object)
  * Added method Collecting.retain(Collection, Object, Object, ...)
  * Added method Collecting.Setup.retain(Object)
  * Added method Collecting.Setup.retain(Object, Object, ...)

### 2.0.3

* Set java language level to 17
* Removed some deprecated items
* Several refinements
* Refined module lazy-narvy ...
  * Added class ReLazy
  * Added class XReLazy
  * Added class InitException
  * Marked class Lazy.InitException as deprecated
* Pinned module io-phobos to 1.25.0
* Pinned module notes-eris to 1.25.0
* Pinned module collection-ceres to 1.25.0

### 1.25.0

* Added module matching-rhea

### 1.24.1

* Pinned module serial-charon to 1.24.0
* Pinned module tuple-janus to 1.24.0

### 1.24.0

* Pinned module decision-leda to 1.23.0
* Added module decision-carpo

### 1.23.0

* Pinned module decision-telesto to 1.22.0
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

* Pinned deprecated module testing-titan to 1.19.2

### 1.19.2

* Marked module testing-titan as deprecated

### 1.19.1

* Refined module arbitrary-mimas
* Pinned deprecated module random-tarvos to 1.19.0

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

* Pinned deprecated module exceptional-01 to 1.13.1
* Prepared site url for multiple major releases

### 1.13.1

* Pinned deprecated module exceptional-01 to 1.13.0
* Pinned deprecated module production-01 to 1.13.0
* Pinned deprecated module random-mimas to 1.13.0
* Pinned deprecated module lazy-01 to 1.13.0
* Pinned deprecated module properties-01 to 1.13.0
* Added module pooling-ariel
  * Marked module pooling-01 as deprecated

### 1.13.0

* Marked module properties-01 as deprecated
* Marked module production-01 as deprecated
* Pinned deprecated module random-01 to 1.12.2
* Pinned deprecated module testing-01 to 1.12.2
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

