package one.digitalinnovation.gof.controller;

import one.digitalinnovation.gof.dto.ClienteDTO;
import one.digitalinnovation.gof.dto.ClienteUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.service.ClienteService;

@RestController
@RequestMapping("clientes")
public class ClienteRestController {

	private final ClienteService clienteService;

    public ClienteRestController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
	public ResponseEntity<Iterable<ClienteDTO>> buscarTodos() {
		Iterable<ClienteDTO> clienteDTO = clienteService.buscarTodos();

		return ResponseEntity.ok(clienteDTO);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ClienteDTO> buscarPorId(@PathVariable Long id) {
		ClienteDTO clienteDTO = clienteService.buscarPorId(id);
		return ResponseEntity.ok(clienteDTO);
	}

	@PostMapping
	public ResponseEntity<Cliente> inserir(@RequestBody ClienteDTO clienteDTO) {
		Cliente cliente = clienteService.inserir(clienteDTO);
		return ResponseEntity.ok(cliente);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> atualizar(@PathVariable Long id, @RequestBody ClienteUpdate clienteUpdate) {
		clienteService.atualizar(id, clienteUpdate);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		clienteService.deletar(id);
		return ResponseEntity.noContent().build();
	}
}
