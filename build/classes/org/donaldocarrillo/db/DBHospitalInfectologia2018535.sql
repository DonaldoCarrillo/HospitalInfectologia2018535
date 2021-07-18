-- Drop database DBHospitalInfectologia2018535;
create database DBHospitalInfectologia2018535;
use DBHospitalInfectologia2018535;

create table Medicos(
		codigoMedico int not null primary key auto_increment,
        licenciaMedica int not null,
        nombres varchar (100),
        apellidos varchar (100),
        horaEntrada varchar(100),
        horaSalida varchar(100),
        turnoMaximo int default 0,
        sexo varchar (20)
);
DELIMITER $$

create procedure sp_AgregarMedicos (p_licenciaMedica int, p_nombres varchar (100), p_apellidos varchar (100), p_horaEntrada varchar(100), p_horaSalida varchar(100), p_sexo varchar (20))
BEGIN
		insert Medicos (licenciaMedica,nombres,apellidos,horaEntrada,horaSalida,sexo)
        values (p_licenciaMedica, p_nombres, p_apellidos, p_horaEntrada, p_horaSalida, p_sexo);
END$$
DELIMITER ;

call sp_AgregarMedicos (8473,'Donaldo','Carrillo','2019-09-01 07:00:00','2019-09-01 13:00:00','Masculino');

select * from Medicos;

DELIMITER $$

create procedure sp_EditarMedicos (p_codigoMedico int, p_licenciaMedica int, p_nombres varchar (100), p_apellidos varchar (100), p_horaEntrada varchar(100), p_horaSalida varchar(100), p_sexo varchar (20))
BEGIN
		update Medicos
        set licenciaMedica = p_licenciaMedica,
        nombres = p_nombres,
        apellidos = p_apellidos,
        horaEntrada = p_horaEntrada,
        horaSalida = p_horaSalida,
        sexo = p_sexo
        where codigoMedico = p_codigoMedico;
END$$
DELIMITER ;

call sp_EditarMedicos (1,7847,'Sebastian','Carrillo','2018-02-03 08:00:00','2018-02-03 14:00:00','Masculino');

select * from Medicos;


DELIMITER $$

create procedure sp_EliminarMedicos (p_codigoMedico int)
BEGIN
		delete from Medicos
        where codigoMedico = p_codigoMedico;
END$$
DELIMITER ;

call sp_EliminarMedicos (5);
call sp_EliminarMedicos (3);
call sp_EliminarMedicos (4);
select * from Medicos;

DELIMITER $$

create procedure sp_ListarMedicos ()
BEGIN
		select C.codigoMedico as codigoMedico, licenciaMedica, nombres, apellidos, horaEntrada, horaSalida, turnoMaximo, sexo
        from Medicos C;
END$$
DELIMITER ;

call sp_ListarMedicos();

select * from Medicos;


DELIMITER $$
create procedure sp_BuscarMedicos(p_codigoMedico int)
BEGIN
		select codigoMedico, nombres, apellidos, licenciaMedica, horaEntrada, horaSalida, turnoMaximo, sexo
			from Medicos
				where codigoMedico = p_codigoMedico;
END$$
DELIMITER ;

call sp_BuscarMedicos(1);

create table Area(
		codigoArea int not null primary key auto_increment,
        nombreArea varchar (45)
);

DELIMITER $$
 
create procedure sp_AgregarAreas (p_nombreArea varchar (45))
BEGIN
		insert Area(nombreArea)
        values (p_nombreArea);
END$$
DELIMITER ;

call sp_AgregarAreas ('Recuperacion');
call sp_AgregarAreas ('Operaciones');

select * from Area;

DELIMITER $$

create procedure sp_EditarAreas (p_codigoArea int, p_nombreArea varchar (45))
BEGIN
		update Area
        set nombreArea = p_nombreArea
        where codigoArea = p_codigoArea;
END$$
DELIMITER ;

call sp_EditarAreas (1,'Enfermos');
call sp_EditarAreas (2,'Consultas');

select * from Area;

DELIMITER $$

create procedure sp_EliminarAreas (p_codigoArea int)
BEGIN
		delete from Area
        where codigoArea = p_codigoArea;
END$$

call sp_EliminarAreas (2);

select * from Area;

DELIMITER ;

DELIMITER $$

create procedure sp_ListarAreas ()
BEGIN
		select C.codigoArea as codigoArea, nombreArea
        from Area C;
END$$
DELIMITER ;

call sp_ListarAreas ();

DELIMITER $$

create procedure sp_BuscarArea(p_codigoArea int)
BEGIN
		select nombreArea
		where codigoArea = p_codigoArea;
END$$
DELIMITER ;

create table Pacientes(
		 codigoPaciente int not null primary key auto_increment,
         DPI varchar(100),
         apellidos varchar (100),
         nombres varchar (100),
         fechaNacimiento date,
         edad int not null,
         direccion varchar (150),
         ocupacion varchar (50),
         sexo varchar (15)
);

DELIMITER $$

create procedure sp_AgregarPacientes (p_DPI varchar(100), p_apellidos varchar (100), p_nombres varchar (100), p_fechaNacimiento date, p_edad int, p_direccion varchar (150), p_ocupacion varchar (50), p_sexo varchar (15))
BEGIN
		insert Pacientes (DPI, apellidos, nombres, fechaNacimiento, edad, direccion, ocupacion, sexo)
		values (p_DPI, p_apellidos, p_nombres, p_fechaNacimiento, p_edad, p_direccion, p_ocupacion, p_sexo);
END$$
DELIMITER ;

call sp_AgregarPacientes ('123485962561','Amezquita Moran','Diego Mario','2002-09-29',16,'Mz K lote 11','Asesor de Ventas','Masculino');
call sp_AgregarPacientes ('851276921486','Carrillo Ron','Paolo Gabriel','2003-05-13',15,'Mnz. M lote 14','Tecnico','Masculino');


select * from Pacientes;

DELIMITER $$

create procedure sp_EditarPacientes (p_codigoPaciente int, p_DPI varchar(100), p_apellidos varchar (100), p_nombres varchar (100), p_fechaNacimiento date, p_edad int, p_direccion varchar (150), p_ocupacion varchar (50), p_sexo varchar (15))
BEGIN
		update Pacientes
        set DPI = p_DPI,
        apellidos = p_apellidos,
        nombres = p_nombres,
        fechaNacimiento = p_fechaNacimiento,
        edad = p_edad,
        direccion = p_direccion,
        ocupacion = p_ocupacion,
        sexo = p_sexo
        where codigoPaciente = p_codigoPaciente;
END$$
DELIMITER ;

call sp_EditarPacientes (1,'7496251463529','Manuel Orozco','Manrique Antonio','2001-04-30',17,'Avenida Lena','Enfermo Auxiliar','Masculino');
call sp_EditarPacientes (2,'9521384922596','Loarca Cifuentes','Aldair Benavente','2000-06-24',18,'Zona 14','Maestro','Masculino');

select * from Pacientes;

DELIMITER $$

create procedure sp_EliminarPacientes (p_codigoPaciente int)
BEGIN
		delete from Pacientes
        where codigoPaciente = p_codigoPaciente;
END$$
DELIMITER ;

call sp_EliminarPacientes (1);

select * from Pacientes;

DELIMITER $$

create procedure sp_ListarPacientes ()
BEGIN
		select C.codigoPaciente as codigoPaciente, DPI, apellidos, nombres, fechaNacimiento, edad, direccion, ocupacion, sexo
        from Pacientes C;
END$$
DELIMITER ;

call sp_ListarPacientes();

DELIMITER $$

create procedure sp_BuscarPaciente(p_codigoPaciente int)
BEGIN	
		select nombres
		where codigoPaciente = p_codigoPaciente;
END$$

DELIMITER ;

create table Especialidades(
		codigoEspecialidad int not null primary key auto_increment,
        nombreEspecialidad varchar (45)
);

DELIMITER $$

create procedure sp_AgregarEspecialidades (p_nombreEspecialidad varchar (45))
BEGIN
		insert Especialidades (nombreEspecialidad)
        values ( p_nombreEspecialidad);
END$$
DELIMITER ;

call sp_AgregarEspecialidades ('Operaciones de apendice');
call sp_AgregarEspecialidades ('Radiografias');

select * from Especialidades;

DELIMITER ;

DELIMITER $$

create procedure sp_EditarEspecialidades (p_codigoEspecialidad int, p_nombreEspecialidad varchar (45))
BEGIN
		update Especialidades
        set nombreEspecialidad = p_nombreEspecialidad
        where codigoEspecialidad = p_codigoEspecialidad;
END$$

call sp_EditarEspecialidades (1,'Operacion de ojos');
call sp_EditarEspecialidades (2,'Operacion de nariz');

select * from Especialidades;

DELIMITER ;

DELIMITER $$

create procedure sp_EliminarEspecialidades (p_codigoEspecialidad int)
BEGIN
		delete from Especialidades
        where codigoEspecialidad = p_codigoEspecialidad;
END$$

call sp_EliminarEspecialidades (1);

select * from Especialidades;

DELIMITER ;

DELIMITER $$

create procedure sp_ListarEspecialidades ()
BEGIN
		select C.codigoEspecialidad as codigoEspecialidad, nombreEspecialidad
        from Especialidades C;
END$$
DELIMITER ;

call sp_ListarEspecialidades();


DELIMITER $$

create procedure sp_BuscarEspecialidad(p_codigoEspecialidad int)
BEGIN
		select nombreEspecialidad
		where codigoEspecialidad = p_codigoEspecialidad;
END$$

DELIMITER ;


create table Horarios(
		codigoHorario int not null primary key auto_increment,
        horarioInicio varchar(100),
        horarioSalida varchar(100),
        lunes int,
        martes int,
        miercoles int,
        jueves int,
        viernes int
);


DELIMITER $$

create procedure sp_AgregarHorarios (p_horarioInicio varchar(100), p_horarioSalida varchar(100), p_lunes int, p_martes int, p_miercoles int, p_jueves int, p_viernes int)
BEGIN
		insert Horarios (horarioInicio,horarioSalida,lunes,martes,miercoles,jueves,viernes)
        values (p_horarioInicio, p_horarioSalida, p_lunes, p_martes, p_miercoles, p_jueves, p_viernes);
END$$
DELIMITER ;


call sp_AgregarHorarios ('2019-04-01 07:00:00','2019-04-01 12:00:00',1,1,1,0,1);
call sp_AgregarHorarios ('2019-05-02 06:00:00','2019-05-02 11:00:00',0,0,1,1,1);

select * from Horarios;

DELIMITER $$

create procedure sp_EditarHorarios (p_codigoHorario int, p_horarioInicio varchar(100), p_horarioSalida varchar(100), p_lunes int, p_martes int, p_miercoles int, p_jueves int, p_viernes int)
BEGIN
		update Horarios
        set horarioInicio = p_horarioInicio,
        horarioSalida = p_horarioSalida,
        lunes = p_lunes,
        martes = p_martes,
        miercoles = p_miercoles,
        jueves = p_jueves,
        viernes = p_viernes
        where codigoHorario = p_codigoHorario;
END$$

call sp_EditarHorarios (1,'2019-05-12 08:30:00','2019-05-12 14:30:00',0,1,1,1,1);
call sp_EditarHorarios (2,'2019-06-05 07:30:00','2019-06-05 13:30:00',1,1,1,1,0);

select * from Horarios;

DELIMITER ;

DELIMITER $$

create procedure sp_EliminarHorarios (p_codigoHorario int)
BEGIN
		delete from Horarios
        where codigoHorario = p_codigoHorario;
END$$ 
DELIMITER ;

call sp_EliminarHorarios (1);

select * from Horarios;

DELIMITER $$

create procedure sp_ListarHorarios ()
BEGIN
		select C.codigoHorario as codigoHorario, horarioInicio, horarioSalida, lunes, martes, miercoles, jueves, viernes
        from Horarios C;
END$$
DELIMITER ;

call sp_ListarHorarios ();

DELIMITER $$

create procedure sp_BuscarHorario(p_codigoHorario int)
BEGIN
		select lunes, martes, jueves
		where codigoHorario = p_codigoHorario;
END$$

DELIMITER ;

create table Cargos(
		codigoCargo int not null primary key auto_increment,
        nombreCargo varchar (45)
);

DELIMITER $$

create procedure sp_AgregarCargos (p_nombreCargo varchar (45))
BEGIN
		insert Cargos (nombreCargo)
        values (p_nombreCargo);
END$$
DELIMITER ;

call sp_AgregarCargos ('Urologia');
call sp_AgregarCargos ('Traumotologia');

select * from Cargos;



DELIMITER $$

create procedure sp_EditarCargos (p_codigoCargo int, p_nombreCargo varchar (45))
BEGIN
		update Cargos
        set nombreCargo = p_nombreCargo
        where codigoCargo = p_codigoCargo;
END$$

call sp_EditarCargos (1,'Oftalmologia');
call sp_EditarCargos (2,'Ginecologo');

select * from Cargos;

DELIMITER ;

DELIMITER $$

create procedure sp_EliminarCargos (p_codigoCargo int)
BEGIN
		delete from Cargos
        where codigoCargo = p_codigoCargo;
END$$

call sp_EliminarCargos (2);

select * from Cargos;

DELIMITER ;

DELIMITER $$

create procedure sp_ListarCargos ()
BEGIN
		select C.codigoCargo as codigoCargo, nombreCargo
        from Cargos C;
END$$
DELIMITER ;

call sp_ListarCargos ();


DELIMITER $$

create procedure sp_BuscarCargo(p_codigoCargo int)
BEGIN
		select nombreCargo 
		where codigoCargo = p_codigoCargo;
END$$

DELIMITER ;

create table MedicoEspecialidad(
		codigoMedicoEspecialidad int not null primary key auto_increment,
        codigoMedico int (10) not null,
        codigoEspecialidad int (10) not null,
        codigoHorario int (10) not null,
		foreign key (codigoMedico) references Medicos (codigoMedico),
        foreign key (codigoEspecialidad) references Especialidades(codigoEspecialidad),
        foreign key (codigoHorario) references Horarios (codigoHorario)
);

DELIMITER $$

create procedure sp_AgregarMedicoEspecialidad (p_codigoMedico int (10), p_codigoEspecialidad int (10), p_codigoHorario int(10))
BEGIN
		insert MedicoEspecialidad (codigoMedico,codigoEspecialidad,codigoHorario)
        values ( p_codigoMedico, p_codigoEspecialidad, p_codigoHorario);
END$$

call sp_AgregarMedicoEspecialidad (2,2,2);

DELIMITER ;

DELIMITER $$

create procedure sp_EditarMedicoEspecialidad (p_codigoMedicoEspecialidad int, p_codigoMedico int (10), p_codigoEspecialidad int (10), p_codigoHorario int (10))
BEGIN
		update MedicoEspecialidad
        set codigoMedico = p_codigoMedico,
        codigoEspecialidad = p_codigoEspecialidad,
        codigoHorario = p_codigoHorario
        where codigoMedicoEspecialidad = p_codigoMedicoEspecialidad;
END$$

call sp_EditarMedicoEspecialidad (1,2,2,2);

DELIMITER ;

DELIMITER $$

create procedure sp_EliminarMedicoEspecialidad (p_codigoMedicoEspecialidad int)
BEGIN
		delete from MedicoEspecialidad
        where codigoMedicoEspecialidad = p_codigoMedicoEspecialidad;
END$$
DELIMITER ;


call sp_EliminarMedicoEspecialidad (1);

DELIMITER $$

create procedure sp_ListarMedicoEspecialidad ()
BEGIN
		select C.codigoMedicoEspecialidad as codigoMedicoEspecialidad, codigoMedico, codigoEspecialidad, codigoHorario
        from MedicoEspecialidad C;
END$$
DELIMITER ;

call sp_ListarMedicoEspecialidad ();

DELIMITER $$
create procedure sp_BuscarMedicoEspecialidad(p_codigoMedicoEspecialidad int)
BEGIN 
		select codigoMedico, codigoEspecialidad
		where codigoMedicoEspecialidad = p_codigoMedicoEspecialidad;
END$$

DELIMITER ;

create table ResponsableTurno(
		codigoResponsableTurno int not null primary key auto_increment,
        nombreResponsable varchar (75),
        apellidosResponsable varchar (45),
        telefonoPersonal varchar (10),
        codigoArea int,
        codigoCargo int,
        foreign key (codigoArea) references Area(codigoArea),
        foreign key (codigoCargo) references Cargos(codigoCargo)
);

DELIMITER $$

create procedure sp_AgregarResponsableTurno (p_nombreResponsable varchar (75), p_apellidosResponsable varchar (45), p_telefonoPersonal varchar (10), p_codigoArea int, p_codigoCargo int)
BEGIN
		insert ResponsableTurno (nombreResponsable,apellidosResponsable,telefonoPersonal,codigoArea,codigoCargo)
        values (p_nombreResponsable, p_apellidosResponsable, p_telefonoPersonal, p_codigoArea, p_codigoCargo);
END$$

call sp_AgregarResponsableTurno ('Joshua','Tepeque Lisco','6076-5325',1,1);

select * from ResponsableTurno;

DELIMITER ;

DELIMITER $$

create procedure sp_EditarResponsableTurno (p_codigoResponsableTurno int, p_nombreResponsable varchar (75), p_apellidosResponsable varchar (45), p_telefonoPersonal varchar (10), p_codigoArea int, p_codigoCargo int)
BEGIN
		update ResponsableTurno
        set nombreResponsable = p_nombreResponsable,
        apellidosResponsable = p_apellidosResponsable,
        telefonoPersonal = p_telefonoPersonal,
        codigoArea = p_codigoArea,
        codigoCargo = p_codigoCargo
        where codigoResponsableTurno = p_codigoResponsableTurno;
END$$

DELIMITER ;

DELIMITER $$

create procedure sp_EliminarResponsableTurno (p_codigoResponsableTurno int)
BEGIN
		delete from ResponsableTurno
        where codigoResponsableTurno = p_codigoResponsableTurno;
END$$

call sp_EliminarResponsableTurno (1);

select * from ResponsableTurno;

DELIMITER ;

DELIMITER $$

create procedure sp_ListarResponsableTurno ()
BEGIN
		select C.codigoResponsableTurno as codigoResponsableTurno, nombreResponsable, apellidosResponsable, telefonoPersonal, codigoArea, codigoCargo
        from ResponsableTurno C;
END$$
DELIMITER ;

call sp_ListarResponsableTurno ();

DELIMITER $$
create procedure sp_BuscarResponsableTurnor(p_codigoResponsableTurno int)
BEGIN
		select telefonoPersonal, nombreResponsable
		where codigoResponsableTurno = p_codigoResponsableTurno;
END$$

DELIMITER ;
create table Turnos(
		codigoTurno int not null primary key auto_increment,
		fechaTurno date,
		fechaCita date,
		valorCita decimal (10,2),
		codigoMedicoEspecialidad int,
		codigoResponsableTurno int,
		codigoPaciente int,
		foreign key (codigoMedicoEspecialidad) references MedicoEspecialidad (codigoMedicoEspecialidad),
		foreign key (codigoPaciente) references Pacientes (codigoPaciente),
        foreign key (codigoResponsableTurno) references ResponsableTurno(codigoResponsableTurno)
);

DELIMITER $$ 
create procedure sp_AgregarTurno(p_fechaTurno varchar(100), p_fechaCita varchar(100), p_valorCita decimal(10,2))
begin
	insert into Turnos(fechaTurno, fechaCita, valorCita)
			values(p_fechaTurno, p_fechaCita, p_valorCita);
end $$ 
DELIMITER ; 

call sp_AgregarTurno('2019/08/06','2019/08/06','70.00');

DELIMITER $$
create procedure sp_ListarTurnos()
begin
	select * from Turnos;
end $$
DELIMITER ;

 
DELIMITER $$ 
create procedure sp_BuscarTurno(p_CodigoTurno int)
begin 
	select codigoTurno,fechaTurno,fechaCita,valorCita,CodigoMedicoEspecialidad,codigoResponsableTurno,codigoPaciente
    from Turnos where (codigoTurno = p_codigoTurno);
end $$ 
DELIMITER ;


DELIMITER $$
create procedure sp_EliminarTurnos(p_codigoTurno int)
begin
	delete from Turnos where (codigoTurno = p_codigoTurno);
end $$
DELIMITER ;


DELIMITER $$ 
create procedure sp_EditarTurno(p_codigoTurno int,p_fechaTurno varchar(100), p_fechaCita varchar(100), p_valorCita varchar(100))
begin
	update Turnos
    set fechaTurno = p_FechaTurno,
    fechaCita = p_fechaCita,
    valorCita = p_valorCita
    where codigoTurno = p_codigoTurno;
end $$
DELIMITER ; 

create table TelefonosMedico(
		codigoTelefonoMedico int not null primary key auto_increment,
        telefonoPersonal varchar (15),
        telefonoTrabajo varchar (15),
        codigoMedico int,
        foreign key (codigoMedico) references Medicos(codigoMedico)
);

DELIMITER $$

create procedure sp_AgregarTelefonosMedico (p_telefonoPersonal varchar (15), p_telefonoTrabajo varchar (15))
BEGIN
		insert TelefonosMedico (telefonoPersonal,telefonoTrabajo)
        values (p_telefonoPersonal, p_telefonoTrabajo);
END$$
DELIMITER ;

call sp_AgregarTelefonosMedico ('5616-8765','2222-5553');

select * from TelefonosMedico;

DELIMITER $$

create procedure sp_EditarTelefonosMedico (p_codigoTelefonoMedico int, p_telefonoPersonal varchar (15), p_telefonoTrabajo varchar (15))-- , p_codigoMedico int)
BEGIN
		update TelefonosMedico
        set telefonoPersonal = p_telefonoPersonal,
        telefonoTrabajo = p_telefonoTrabajo
        -- codigoMedico = p_codigoMedico
        where codigoTelefonoMedico = p_codigoTelefonoMedico;
END$$
DELIMITER ;

call sp_EditarTelefonosMedico (1,'5616-7800','2222-5553');


DELIMITER $$

create procedure sp_EliminarTelefonosMedico (p_codigoTelefonoMedico int)
BEGIN
		delete from TelefonosMedico
        where codigoTelefonoMedico = p_codigoTelefonoMedico;
END$$
DELIMITER ;

call sp_EliminarTelefonosMedico(1);


DELIMITER $$

create procedure sp_ListarTelefonosMedico ()
BEGIN
		select C.codigoTelefonoMedico as codigoTelefonoMedico, telefonoPersonal, telefonoTrabajo, codigoMedico
        from TelefonosMedico C;
END$$
DELIMITER ;

call sp_ListarTelefonosMedico;


DELIMITER $$

create procedure sp_BuscarTelefonoMedico(p_codigoTelefonoMedico int)
BEGIN
		select telefonoPersonal, telefonoTrabajo
		where codigoTelefonoMedico = p_codigoTelefonoMedico;
END$$ 

DELIMITER ;

create table ContactoUrgencia(
		codigoContactoUrgencia int not null primary key auto_increment,
        nombres varchar (100),
        apellidos varchar (100),
        numeroContacto varchar (10),
        codigoPaciente int,
        foreign key (codigoPaciente) references Pacientes (codigoPaciente)
);

DELIMITER $$

create procedure sp_AgregarContactoUrgencia (p_nombres varchar (100), p_apellidos varchar (100), p_numeroContacto varchar (10))
BEGIN
		insert ContactoUrgencia (nombres,apellidos,numeroContacto)
        values (p_nombres, p_apellidos, p_numeroContacto);
END$$
DELIMITER ;

call sp_AgregarContactoUrgencia ('Sucely','Enriquez Vela','7896-4583');

select * from ContactoUrgencia;

DELIMITER $$

create procedure sp_EditarContactoUrgencia (p_codigoContactoUrgencia int, p_nombres varchar (100), p_apellidos varchar (100), p_numeroContacto varchar (10))
BEGIN
		update ContactoUrgencia
        set nombres = p_nombres,
        apellidos = p_apellidos,
        numeroContacto = p_numeroContacto
        where codigoContactoUrgencia = p_codigoContactoUrgencia;
END$$
DELIMITER ;

call sp_EditarContactoUrgencia(1, 'Luis', 'Enriquez Girard','5870-3015');

DELIMITER $$

create procedure sp_EliminarContactoUrgencia (p_codigoContactoUrgencia int)
BEGIN
		delete from ContactoUrgencia
        where codigoContactoUrgencia = p_codigoContactoUrgencia;
END$$

call sp_EliminarContactoUrgencia (1);

select * from ContactoUrgencia;

DELIMITER ;

DELIMITER $$

create procedure sp_ListarContactoUrgencia ()
BEGIN
		select C.codigoContactoUrgencia as contactoUrgencia, nombres, apellidos,numeroContacto, codigoPaciente
        from ContactoUrgencia C;
END$$

call sp_ListarContactoUrgencia ();

DELIMITER ;

DELIMITER $$

create procedure sp_BuscarContactoUrgencia(p_codigoContactoUrgencia int)
BEGIN
		select codigoPaciente
		where codigoContactoUrgencia = p_codigoContactoUrgencia ;
END$$

DELIMITER ;


select * from Horarios;

create table Usuarios(
	codigoUsuario int not null primary key auto_increment,
    usuarioLogin varchar(45),
    usuarioContrasena varchar(45),
    usuarioEstado tinyint,
    usuarioFecha date,
    usuarioHora time
);

DELIMITER $$
create procedure sp_AgregarUsuario(p_usuarioLogin varchar(45), p_usuarioContrasena varchar(45), p_usuarioEstado tinyint, p_usuarioFecha date, p_usuarioHora time)
begin
	insert into Usuarios(usuarioLogin, usuarioContrasena, usuarioEstado, usuarioFecha, usuarioHora)
			values(p_usuarioLogin, p_usuarioContrasena, p_usuarioEstado, p_usuarioFecha, p_usuarioHora);
End $$
Delimiter ;

call sp_AgregarUsuario('Donaldo', 'admin', 0, '2019-07-08','09:41:00' );
Delimiter $$
create procedure sp_EditarUsuario(p_codigoUsuario int, p_usuarioLogin varchar(45), p_usuarioContrasena varchar(45), p_usuarioEstado tinyint, p_usuarioFecha date, p_usuarioHora time)
begin
	update Usuarios 
		set usuarioLogin = p_usuarioLogin,
            usuarioContrasena = p_usuarioContrasena,
            usuarioEstado = p_usuarioEstado,
            usuarioFecha = p_usuarioFecha,
            usuarioHora = p_usuarioHora
		where codigoUsuario = p_codigoUsuario;

End $$
Delimiter ;

Delimiter $$
create procedure sp_EliminarUsuarios(p_codigoUsuario int)
begin
	delete from Usuarios
    where codigoUsuario = p_CodigoUsuario;
end $$
Delimiter ;

call sp_EliminarUsuarios(1);
call sp_EliminarUsuarios(2);

Delimiter $$
create procedure sp_ListarUsuarios()
begin
	select C.codigoUsuario as codigoUsuario, usuarioLogin, usuarioContrasena, usuarioEstado, usuarioFecha, usuarioHora
    from Usuarios C;
end $$
Delimiter ;

call sp_ListarUsuarios;

Delimiter $$
create procedure sp_BuscarUsuarios(p_codigoUsuario int)
begin
	select codigoUsuario, usuarioContrasena, usuarioEstado, usuarioFecha, usuarioHora
    from Usuarios
    where codigoUsuario = p_codigoUsuario;
end $$
Delimiter ;

call sp_BuscarUsuarios(1);

create table tipoDeUsuario(
	codigoTipoUsuario int not null primary key auto_increment,
    nombre varchar(100),
    descripcion varchar(150)
);

Delimiter $$
create procedure sp_AgregarTipoDeUsuario(p_nombre varchar(100), p_descripcion varchar(150))
begin
	insert into tipoDeUsuario(nombre, descripcion)
		values(p_nombre, p_descripcion);
end $$
Delimiter ;

call sp_AgregarTipoDeUsuario('Administrador', 'Administrador');
call sp_AgregarTipoDeUsuario('root','root');
call sp_AgregarTipoDeUsuario('Invitado','Invitado');

Delimiter $$
create procedure sp_EditarTipoUsuario(p_codigoTipoUsuario int, p_nombre varchar(100), p_descripcion varchar(150))
begin
	update	tipoDeUsuario
		set	nombre = p_nombre,
			descripcion = p_descripcion
				where p_codigoTipoUsuario = codigoUsario;
end$$
Delimiter ;

Delimiter $$
create procedure sp_EliminarTipoDeUsuario(p_codigoTipoDeUsuario int)
begin 
	delete from tipoDeUsuario
		where codigoTipoDeUsuario = p_codigoTipoUsuarios;
end$$
Delimiter ;

Delimiter $$
create procedure sp_ListarTipoDeUsuarios()
begin
	select C.codigoTipoUsuario as codigoTipoUsuario, nombre, descripcion
		from tipoDeUsuario C;
end $$
Delimiter ;

call sp_ListarTipoDeUsuarios;

Delimiter $$
create procedure sp_BuscarTipoDeUsuario(p_codigoTipoDeUsuario int)
begin
	select codigoTipoDeUsuario, nombre, descripcion
		from tipoDeUsuario
			where codigoTipoDeUsuario = p_codigoTipoDeUsuario;
end $$
Delimiter ;

create table controlCitas(
	codigoControlCita int primary key auto_increment,
    fecha date,
    horaInicio varchar(100),
    horaFin varchar(100),
    codigoMedico int,
    codigoPaciente int,
    foreign key(codigoMedico) references Medicos(codigoMedico),
    foreign key(codigoPaciente) references Pacientes(codigoPaciente)
);

-- Drop procedure sp_AgregarControlCitas;
Delimiter $$
create procedure sp_AgregarControlCitas(p_fecha date, p_horaInicio varchar(100), p_horaFin varchar(100), p_codigoMedico int, p_codigoPaciente int)
begin
	insert into controlCitas(fecha, horaInicio, horaFin, codigoMedico, codigoPaciente)
			value(p_fecha, p_horaInicio, p_horaFin, p_codigoMedico, p_codigoPaciente);
end $$
Delimiter ;

call sp_AgregarControlCitas('2019-08-08', '19:00:00', '06:00:00',1,2);
call sp_AgregarControlCitas('2019-09-08', '07:00:00', '18:00:00',1,2);

drop procedure sp_EliminarControlCitas;
Delimiter $$
create procedure sp_EliminarControlCitas(p_codigoControlCita int)
begin
	delete from controlCitas
			where p_codigoControlCita = codigoControlCita;
end$$
Delimiter ;

call sp_EliminarControlCitas(1);

Drop procedure sp_EditarControlCitas;
Delimiter $$
create procedure sp_EditarControlCitas(p_codigoControlCita int, p_fecha varchar(100), p_horaInicio varchar(100), p_horaFin varchar(100), p_codigoMedico int, p_codigoPaciente int)
begin
	update controlCitas
		set fecha = p_fecha,
			horaInicio = p_horaInicio,
            horaFin = p_horaFin,
            codigoMedico = p_codigoMedico,
            codigoPaciente = p_codigoPaciente
			where p_codigoControlCita = codigoControlCita;
end $$
Delimiter ;

call sp_EditarControlCitas(2,'2019-10-08', '07:30:00', '21:00:00',1,2);

-- drop procedure sp_ListarControlCitas;
Delimiter $$
create procedure sp_ListarControlCitas()
begin
	select C.codigoControlCita as codigoControlCita, horaInicio, horaFin, codigoMedico, codigoMedico, codigoPaciente
		from controlCitas C;
end$$
Delimiter ;

call sp_ListarControlCitas;

-- drop procedure sp_BuscarControlCitas;
Delimiter $$
create procedure sp_BuscarControlCitas(p_codigoControlCita int)
begin
	select horaInicio, horaFin, codigoMedico, codigoMedico, codigoPaciente
		from controlCitas
			where p_codigoControlCita = codigoControlCita;
end $$
Delimiter ;

call sp_BuscarControlCitas(3);

create table Recetas(
	codigoReceta int not null primary key auto_increment,
    descripcionReceta varchar(45),
    codigoControlCita int,
    foreign key (codigoControlCita)	references controlCitas (codigoControlCita)
);

Delimiter $$
create procedure sp_AgregarRecetas(p_descripcionReceta varchar(45), p_codigoControlCita int)
begin
	insert into Recetas(descripcionReceta, codigoControlCita)
		values(p_descripcionReceta, p_codigoControlCita);
end $$
Delimiter ;

call sp_AgregarRecetas('Acetaminofen 35 gramos', 3);
call sp_AgregarRecetas('Amoxicilina 45 gramos', 4);

Delimiter $$
create procedure sp_EliminarRecetas(p_codigoReceta int)
begin
	delete from recetas
		where p_codigoReceta = codigoReceta;
end $$
Delimiter ;

call sp_EliminarRecetas(2);

Delimiter $$
create procedure sp_EditarRecetas(p_codigoReceta int, p_descripcionReceta varchar(45), p_codigoControlCita int)
begin
	update Recetas
		set descripcionReceta = p_descripcionReceta,
			codigoControlCita = p_codigoControlCita
				where p_codigoReceta = codigoReceta;
end $$
Delimiter ;

call sp_EditarRecetas(1,'Amoxicilina 35 gramos', 5);

Delimiter $$
create procedure sp_ListarRecetas()
begin
	select C.codigoReceta as codigoReceta, descripcionReceta, codigoControlCita
		from Recetas C;
end $$
Delimiter ;

call sp_ListarRecetas();

Delimiter $$
create procedure sp_BuscarReceta(p_codigoReceta int)
begin
	select codigoReceta, descripcionReceta, codigoControlCita
		from Recetas
			where p_codigoReceta = codigoReceta;
end $$
Delimiter ;

call sp_BuscarReceta(1);