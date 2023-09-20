package ar.unrn.tp.web;

import java.time.LocalDateTime;

public record ErrorDetail(LocalDateTime timestamp, String message) {

}
