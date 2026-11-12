Programacion Orientada a Objetos II
Trabajo Práctico Integrador — UNQTravel

Integrantes: 
- Matias Ferrufino
- Agustina Loray
- Octavio Pastorive 

## Decisiones de diseño

### Reporte de exposición por proveedor

El reporte mide qué proporción del catálogo depende de cada proveedor. Cada
entrada de primer nivel del catálogo aporta una unidad al total, incluidos los
paquetes. Un servicio individual se atribuye al proveedor informado al
construirlo.

Como un paquete puede reunir servicios de distintos proveedores, no se asigna
todo su peso a uno solo ni se cuenta una unidad completa para cada proveedor.
En cambio, se recorren también los paquetes anidados, se reúnen los proveedores
distintos de sus servicios componentes y se reparte el peso del paquete en
partes iguales entre ellos. Si varios componentes pertenecen al mismo
proveedor, ese proveedor participa una sola vez en el reparto. De esta manera,
cada entrada del catálogo sigue aportando exactamente una unidad y los
porcentajes del reporte suman el 100 % (salvo diferencias mínimas de
representación decimal).

Si un ítem no tiene proveedor informado, su peso se atribuye al grupo
`Sin proveedor asignado`, para no excluirlo del total ni interrumpir la
generación del reporte. Una composición cíclica de paquetes se considera
inválida y se rechaza explícitamente.

### Excepciones del dominio

Las validaciones y los errores de operación propios del dominio se representan
con `Excepciones`, que conserva el mensaje y, cuando corresponde, la causa
original. `NotificacionPagoException` es un subtipo específico: permite
distinguir un pago ya ejecutado cuya notificación posterior falló. Los errores
de APIs externas se traducen a la excepción propia en el límite del flujo de
pago, mientras que los errores de las colecciones inmutables de Java siguen
siendo los definidos por la JDK.

## Pruebas y cobertura

Ejecutar la suite completa y verificar los umbrales de cobertura con:

```sh
mvn clean verify
```

JaCoCo controla una cobertura global mínima del 95 % de líneas para el código
de producción y mantiene además un mínimo del 80 % de líneas para los paquetes
de pago. La medición limpia más reciente alcanzó 99,2 % de líneas (772 de 778)
con 110 pruebas, sin fallos ni errores. La cobertura se calcula sobre las
clases de producción instrumentadas por JaCoCo; no es una medición de ramas.

Las pruebas principales cubren:

- Estados y reservas: transiciones permitidas y rechazadas, pagos confirmados,
  bloqueo y liberación de cupos, reembolsos, notificación a observadores y
  recuperación ante fallos parciales.
- Catálogo y paquetes: búsqueda con criterios simples y compuestos, validación
  de atributos, cálculo de precios con descuentos, disponibilidad de cupos y
  rollback cuando falla una operación compuesta.
- Reportes: ranking por unidades y precio cobrado, ocupación por categoría,
  exposición ponderada entre proveedores, paquetes anidados, ciclos y catálogo
  vacío.
- Formatos: exportación de los tres reportes a texto plano, CSV y HTML,
  incluidas las salidas sin filas.
- Pagos y traslados: ejecución, fallos de transacción o notificación, limpieza
  de cupos, cálculos y adaptadores asociados.
