# Seguridad
## Primer escenario

### Herramienta utilizada para la demostración

#### Acunetix

Acunetix es un escáner de seguridad de aplicaciones web automatizado y una plataforma de gestión de vulnerabilidades.

#### Acunetix Scan

Permite analizar un objetivo entero empezando desde una URL y mapea toda su estructura completa
El Scanner testeará las paginas de la URL para encontrar vulnerabilidades. Permite una cobertura de escaneo rapida y facilmente personalizable

#### Acunetix Reports

* Dashboard en tiempo real con tendencias
* Genera una gran variedad de reportes de administración y de conformidad incluyendo OWASP Top 10 , PCI DSS , ISO 27001 , HIPAA
* Es posible exportar los reportes a XML y pueden ser consumidos por otros sistemas

### Demostración

#### Análisis en busca de vulnerabilidades

Se realizó un analísis completo de vulnerabilidades utilizando Acunetix Scan. Se obtuvieron 6 vulnerabilidades de tipo medio que potencialmente pueden ser críticas para la aplicación.

#### Campo de contraseña enviado mediante el método GET

Observamos que el campo de contraseña es enviado mediante el método GET en una de las páginas

Esto ocurre en la pagina del login (/authenticate.html)

También ocurre en la página de registro (/signup.html)

Adicionalmente observamos su potencial impacto , clasificación y modo de corrección.

### Análisis después de corrección de vulnerabilidades

#### Campo de contraseña enviado mediante el método GET

En primer lugar , se realizó la corrección de la vulnerabilidad específicando el método POST en cada una de las formas donde se registraba la contraseña

En la forma de registro

En la forma de logueo

Posteriormente , se volvió a realizar el escaner y se obtuvieron los siguientes resultados con dos vulnerabilidades medias menos.

De esta manera , observamos que las vulnerabilidades de campo de contraseña enviado mediante el método GET ya no se encuentran.



