package io.github.mateus81.mensagensapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.mateus81.mensagensapi.model.entity.Mensagem;

public interface MensagemRepository extends JpaRepository<Mensagem, Integer> {
}
