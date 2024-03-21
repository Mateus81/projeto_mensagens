package io.github.mateus81.mensagensapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.mateus81.mensagensapi.model.entity.Conversa;

public interface ConversaRepository extends JpaRepository<Conversa, Integer>{

}
