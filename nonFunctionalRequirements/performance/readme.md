# Rendimiento
## Primer escenario

### Herramienta utilizada para la demostración

#### Jmeter

JMeter es un proyecto de Apache que puede ser utilizado como una herramienta de prueba de carga para analizar y medir el rendimiento de una variedad de servicios, con énfasis en aplicaciones web.

### Caso

Para analizar el escenario se utilizó una petición GET a la url `https://teach2-me.herokuapp.com/api/v1/classes?name=CLASE` con un token de seguridad obtenido del proceso de login de la aplicación.

[Ejemplo de resultado](response.json)						

### Antes de implementar el caché

El resultado con 20 peticiones simultaneas fue de más de cuatro segundos, a continuación se presenta el ejemplo de la petición con JMeter.

![](images/sincache.PNG)

Analizando varias cantidades de peticiones se concluyó que el tiempo promedio de resultados es mayor a un segundo.

![](images/antes1.PNG)

Además, al incrementar la cantidad de peticiones, se distingue que se incrementa el tiempo de respuesta, esto implica que para grandes cantidades de solicitudes concurrentes, como las que debe manejar una aplicación web como esta, se debe implementar un sistema de caché para asegurar el rendimiento efectivo de las consultas de clases.

![](images/antes2.PNG)

### Después de implementar el caché

Después de haber implementado un caché Redis, el resultado con 20 peticiones simultaneas fue de 1.8 segundos, a continuación se presenta el ejemplo de la petición con JMeter.

![](images/concache.PNG)

Analizando varias cantidades de peticiones se concluyó que el tiempo promedio de resultados es menor a un segundo.

![](images/despues1.PNG)

Y al incrementar la cantidad de peticiones se observa un comportamiento mas constante, esto nos permite demostrar que el rendimiento de la aplicación se incrementó para la consulta de clases.

![](images/despues2.PNG)

### Conclusión

El uso de un caché Redis nos permitió cumplir el escenario planteado, obtiendo consultas de varias clases menos de un segundo, además nos permite tener centralizada esta clase información optimizando su acceso de lectura en caso de tener que realizar escalabilidad horizontal en el futuro.

![](images/antes3.PNG)

El proceso de autenticación y búsqueda con 20 usuarios inicialmente tomó 4 segundos.

![](images/despues3.PNG)

Pero al implementar el caché Redis todo el proceso de autnticación y consulta de las clases toma un tiempo promedio de 2 segundos, optimizando el uso de la aplicación.
