# Seguridad
## Primer escenario

### Herramienta utilizada para la demostración

#### Acunetix

Acunetix es un escáner de seguridad de aplicaciones web automatizado y una plataforma de gestión de vulnerabilidades.

![](https://github.com/Rock3tTeam/Teach-me/blob/master/nonFunctionalRequirements/security/images/acunetix.png)

#### Acunetix Scan

Permite analizar un objetivo entero empezando desde una URL y mapea toda su estructura completa
El Scanner testeará las paginas de la URL para encontrar vulnerabilidades. Permite una cobertura de escaneo rapida y facilmente personalizable

![](https://github.com/Rock3tTeam/Teach-me/blob/master/nonFunctionalRequirements/security/images/acunetixscan.png)

#### Acunetix Reports

* Dashboard en tiempo real con tendencias
* Genera una gran variedad de reportes de administración y de conformidad incluyendo OWASP Top 10 , PCI DSS , ISO 27001 , HIPAA
* Es posible exportar los reportes a XML y pueden ser consumidos por otros sistemas

![](https://github.com/Rock3tTeam/Teach-me/blob/master/nonFunctionalRequirements/security/images/acunetixreports.png)


### Demostración

#### Análisis en busca de vulnerabilidades

Se realizó un analísis completo de vulnerabilidades utilizando Acunetix Scan. Se obtuvieron 6 vulnerabilidades de tipo medio que potencialmente pueden ser críticas para la aplicación.

![](https://github.com/Rock3tTeam/Teach-me/blob/master/nonFunctionalRequirements/security/images/get-password-scan-before.PNG)

![](https://github.com/Rock3tTeam/Teach-me/blob/master/nonFunctionalRequirements/security/images/get-password-scan-before2.PNG)


#### Campo de contraseña enviado mediante el método GET

Observamos que el campo de contraseña es enviado mediante el método GET en una de las páginas

![](https://github.com/Rock3tTeam/Teach-me/blob/master/nonFunctionalRequirements/security/images/get-password-scan-before3.png)

Esto ocurre en la pagina del login (/authenticate.html)

![](https://github.com/Rock3tTeam/Teach-me/blob/master/nonFunctionalRequirements/security/images/get-password-scan-before4.PNG)

También ocurre en la página de registro (/signup.html)

![](https://github.com/Rock3tTeam/Teach-me/blob/master/nonFunctionalRequirements/security/images/get-password-scan-before5.PNG)

Adicionalmente observamos su potencial impacto , clasificación y modo de corrección.

![](https://github.com/Rock3tTeam/Teach-me/blob/master/nonFunctionalRequirements/security/images/get-password-scan-before6.PNG)

### Tipo Password input con autocompletado activo

Tenemos también vulnerabilidades de tipo informativo. En este caso observamos que existen campos de contraseña con autocompletado activo que pueden ser peligrosos en caso de que un atacante tenga acceso local en un dispositivo desde el cual se haya ingresado a la aplicación

![](https://github.com/Rock3tTeam/Teach-me/blob/master/nonFunctionalRequirements/security/images/input-password-autocomplete-before.PNG)

#### Análisis después de corrección de vulnerabilidades

#### Campo de contraseña enviado mediante el método GET

En primer lugar , se realizó la corrección de la vulnerabilidad específicando el método POST en cada una de las formas donde se registraba la contraseña

En la forma de registro

![](https://github.com/Rock3tTeam/Teach-me/blob/master/nonFunctionalRequirements/security/images/correccion-get-password1.PNG)

En la forma de logueo

![](https://github.com/Rock3tTeam/Teach-me/blob/master/nonFunctionalRequirements/security/images/correccion-get-password2.PNG)

Posteriormente , se volvió a realizar el scan y se obtuvieron los siguientes resultados con dos vulnerabilidades medias menos.

![](https://github.com/Rock3tTeam/Teach-me/blob/master/nonFunctionalRequirements/security/images/get-password-scan-after.PNG)

De esta manera , observamos que las vulnerabilidades de campo de contraseña enviado mediante el método GET ya no se encuentran.

![](https://github.com/Rock3tTeam/Teach-me/blob/master/nonFunctionalRequirements/security/images/get-password-scan-after2.PNG)

### Tipo Password input con autocompletado activo

En primer lugar , se realizó la corrección de la vulnerabilidad deshabilitando el autocompletado en los campos de contraseña

En la forma de registro

![](https://github.com/Rock3tTeam/Teach-me/blob/master/nonFunctionalRequirements/security/images/input-password-autocomplete-after.PNG)

En la forma de logueo

![](https://github.com/Rock3tTeam/Teach-me/blob/master/nonFunctionalRequirements/security/images/input-password-autocomplete-after2.PNG)

Posteriormente , se volvió a realizar el scan y se observa que ya no se obtienen dichas vulnerabilidades

![](https://github.com/Rock3tTeam/Teach-me/blob/master/nonFunctionalRequirements/security/images/input-password-autocomplete-after3.PNG)


## HTML sin protección CSRF

Se decidió ignorar las vulnerabilidades de CSRF en los campos de logueo y registro ya que cualquier usuario debe poder acceder a dichas paginas. Adicionalmente , una vez el usuario se haya logueado , todas las paginas realizan una validación de seguridad mediante un JSON Web Token que permite proteger a la aplicación contra este tipo de ataques.

![](https://github.com/Rock3tTeam/Teach-me/blob/master/nonFunctionalRequirements/security/images/html-without-CSRF-protection-before.PNG)

### Comparación entre scans

Tenemos la posibilidad de obtener una comparación entre los scans realizados hasta el momento y observar como se han ido reduciendo las vulnerabilidades

![](https://github.com/Rock3tTeam/Teach-me/blob/master/nonFunctionalRequirements/security/images/scan-compare.PNG)







