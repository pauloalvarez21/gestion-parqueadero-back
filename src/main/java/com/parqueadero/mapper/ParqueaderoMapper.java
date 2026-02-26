package com.parqueadero.mapper;

import com.parqueadero.dto.EspacioDTO;
import com.parqueadero.dto.TicketDTO;
import com.parqueadero.dto.VehiculoDTO;
import com.parqueadero.entity.Espacio;
import com.parqueadero.entity.Ticket;
import com.parqueadero.entity.Vehiculo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParqueaderoMapper {

    TicketDTO toTicketDTO(Ticket ticket);

    VehiculoDTO toVehiculoDTO(Vehiculo vehiculo);

    @Mapping(target = "ocupado", expression = "java(espacio.getEstado() == com.parqueadero.enums.EstadoEspacio.OCUPADO)")
    EspacioDTO toEspacioDTO(Espacio espacio);
}