package one.digitalinnovation.gof.service;

import one.digitalinnovation.gof.dto.ClienteDTO;
import one.digitalinnovation.gof.dto.ClienteUpdate;
import one.digitalinnovation.gof.model.Cliente;

public interface ClienteService {

	Iterable<ClienteDTO> buscarTodos();

	ClienteDTO buscarPorId(Long id);

	Cliente inserir(ClienteDTO clienteDTO);

	void atualizar(Long id, ClienteUpdate clienteUpdate);

	void deletar(Long id);

}
