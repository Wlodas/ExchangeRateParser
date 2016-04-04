# ExchangeRateParser

Job application project for Intitek company.

Instead of originally required console interface a very simple JavaFX GUI has been implemented. After building project with Maven application can be started just from a JAR file.

Application window allows to input 3 parameters: currency code (e.g.: EUR), period between 2 dates. After successful search operation result window shows up displaying mean buying rate and selling rate standard deviation. In case of no data being found "NaN" value is shown.

Used technologies and libraries:
- JDK 8
- Lombok
- jOOL
- Log4j 2
- JavaFX
- Guice
- Apache Commons Math
- Jersey Client
- JAXB
- TestNG
- AssertJ
- Mockito

IDE requirements:
- Lombok plugin
- TestNG support (tests launched from IDE only)