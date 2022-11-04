import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProcesarEmpleados {

    // Procesamiento de flujos de objetos Empleado.
    static List<Empleado> empleados;

    public static void main(String[] args) throws IOException {
        inicializarArreglo();
        lista4a6(); //Colección de empleados cuyo salario se encuentre en un rango determinado.
        sumaPorDepartamento(); //Colección de empleados que pertenecen a cada departamento
                               //Nombre del empleado que más gana de cada departamento
        contadorDeEmpleados(); //Cantidad de empleados por departamento.
        nominaPorDepartamento(); //Sumatoria de la nómina por departamento.
        empleadoMayor(); //Nombre del empleado que mayor salario recibe (de todos).
        empleadoMenor(); //• Nombre del empleado que menor salario recibe (de todos)
        promedioPorDepartamento(); //Promedio de salario por departamento.
        promedio(); //Promedio salario general.
        nominaTotal(); //Valor total de la nómina.


    }


    // inicializa arreglo de objetos Empleado
    public static void inicializarArreglo() throws IOException {
        Pattern pattern = Pattern.compile(";");
        String fileName = "empleado.csv";
        try (Stream<String> lines = Files.lines(Path.of(fileName))) {
            empleados = lines.skip(1).map(line -> {
                String[] arr = pattern.split(line);
                return new Empleado(arr[0], arr[1], arr[2], Double.parseDouble(arr[3]), arr[4]);
            }).collect(Collectors.toList());

        }
    }


    // Predicado que devuelve true para salarios en el rango $4000-$6000

    static Predicate<Empleado> cuatroASeisMil =
            e -> (e.getSalario() >= 4000 && e.getSalario() <= 6000);


    // Muestra los empleados con salarios en el rango $4000-$6000
    // en orden ascendente por salario
    public static void lista4a6() {
        System.out.printf(
                "%nEmpleados que ganan $4000-$6000 mensuales ordenados por salario:%n");
        empleados.stream()
                .filter(cuatroASeisMil)
                .sorted(Comparator.comparing(Empleado::getSalario))
                .forEach(System.out::println);
    }



    // cuenta el número de empleados en cada departamento
    public static void contadorDeEmpleados() {
        System.out.printf("%nConteo de empleados por departamento:%n");
        Map<String, Long> conteoEmpleadosPorDepartamento =
                empleados.stream()
                        .collect(Collectors.groupingBy(Empleado::getDepartamento,
                                TreeMap::new, Collectors.counting()));
        conteoEmpleadosPorDepartamento.forEach(
                (departamento, conteo) -> System.out.printf(
                        "%s tiene %d empleado(s)%n", departamento, conteo));
    }

    // suma de salarios de empleados con el método sum de DoubleStream
    public static void sumarSalarios() {
        System.out.printf(
                "%nSuma de los salarios de los empleados (mediante el metodo sum): %.2f%n",
                empleados.stream()
                        .mapToDouble(Empleado::getSalario)
                        .sum());
    }

    // calcula la suma de los salarios de los empleados con el método reducede Stream
    public static void nominaTotal() {
        System.out.printf("%nNomina Total: %.2f%n",
                empleados.stream()
                        .mapToDouble(Empleado::getSalario)
                        .reduce(0, (valor1, valor2) -> valor1 + valor2));
    }

    // promedio de salarios de empleados con el método average de DoubleStream
    public static void promedio() {
        System.out.printf("%nPromedio total: %.2f%n",
                empleados.stream()
                        .mapToDouble(Empleado::getSalario)
                        .average()
                        .getAsDouble());
    }

    public static void sumaPorDepartamento() {
        System.out.printf("%nEmpleados por departamento:%n");
        Map<String, List<Empleado>> agrupadoPorDepartamento =
                empleados.stream()
                        .collect(Collectors.groupingBy(Empleado::getDepartamento));
        agrupadoPorDepartamento.forEach(
                (departamento, empleadosEnDepartamento) ->
                {
                    System.out.println(departamento + "\n Empleado que mas gana: " +
                            empleadosEnDepartamento.stream().max(Comparator.comparing(Empleado::getSalario)).get());
                    empleadosEnDepartamento.forEach(
                            empleado -> System.out.printf(" %s%n", empleado));
                }
        );
    }
    public static void promedioPorDepartamento() {
        System.out.printf("%nPromedio por departamento:%n");
        Map<String, List<Empleado>> promedioPorDepartamento =
                empleados.stream()
                        .collect(Collectors.groupingBy(Empleado::getDepartamento));
        promedioPorDepartamento.forEach(
                (departamento, empleadosEnDepartamento) ->
                {
                    System.out.println("\n"+departamento + "\nPromedio del departamento: " +
                            empleadosEnDepartamento.stream().mapToDouble(Empleado::getSalario)
                                    .average()
                                    .getAsDouble());
                    ;
                }
        );
    }

    public static void nominaPorDepartamento() {
        System.out.printf("%nNomina por departamento:%n");
        Map<String, List<Empleado>> nominaPorDepartamento =
                empleados.stream()
                        .collect(Collectors.groupingBy(Empleado::getDepartamento));
        nominaPorDepartamento.forEach(
                (departamento, empleadosEnDepartamento) ->
                {
                    System.out.println("\n"+departamento + "\nNomina del departamento: " +
                            empleadosEnDepartamento.stream().mapToDouble(Empleado::getSalario).sum());
                    ;
                }
        );
    }

    public static void empleadoMayor() {
        System.out.printf("%nEmpleado que mas gana:%n%s%n",
                empleados.stream()
                        .max(Comparator.comparing(Empleado::getSalario)).get());
    }

    public static void empleadoMenor() {
        System.out.printf("%nEmpleado que menos gana:%n%s%n",
                empleados.stream()
                        .min(Comparator.comparing(Empleado::getSalario)).get());
    }
}