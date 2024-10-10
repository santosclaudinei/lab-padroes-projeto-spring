package one.digitalinnovation.gof.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import one.digitalinnovation.gof.dto.ClienteDTO;
import one.digitalinnovation.gof.dto.ClienteUpdate;
import one.digitalinnovation.gof.dto.EnderecoDTO;
import org.springframework.stereotype.Service;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.repository.ClienteRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.repository.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.ViaCepService;

import javax.persistence.EntityNotFoundException;

@Service
public class ClienteServiceImpl implements ClienteService {

	private final ClienteRepository clienteRepository;
	private final EnderecoRepository enderecoRepository;
	private final ViaCepService viaCepService;

	public ClienteServiceImpl(ClienteRepository clienteRepository, EnderecoRepository enderecoRepository, ViaCepService viaCepService) {
		this.clienteRepository = clienteRepository;
		this.enderecoRepository = enderecoRepository;
		this.viaCepService = viaCepService;
	}

	@Override
	public Iterable<ClienteDTO> buscarTodos() {
		Iterable<Cliente> clientes = clienteRepository.findAll();
		List<ClienteDTO> clienteDTOList = new ArrayList<>();

		for (Cliente cliente : clientes) {
			ClienteDTO clienteDTO = converteClienteParaDTO(cliente);
			clienteDTOList.add(clienteDTO);
		}

		return clienteDTOList;
	}

	@Override
	public ClienteDTO buscarPorId(Long id) {
		Optional<Cliente> cliente = clienteRepository.findById(id);

		if (cliente.isPresent())
			return converteClienteParaDTO(cliente.get());

		throw new EntityNotFoundException("Cliente com ID " + id + " não encontrado");
	}

	@Override
	public Cliente inserir(ClienteDTO clienteDTO) {
		Cliente cliente = salvarClienteDTOComCep(clienteDTO);

		return clienteRepository.save(cliente);
	}

	@Override
	public void atualizar(Long id, ClienteUpdate clienteUpdate) {
		Cliente clienteExistente = clienteRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Cliente com ID " + id + " não encontrado"));

		clienteExistente.setNome(clienteUpdate.getNome());
		clienteExistente.setEndereco(buscarOuCriarEndereco(clienteUpdate.getCep()));

		clienteRepository.save(clienteExistente);
	}

	@Override
	public void deletar(Long id) {
		if (!clienteRepository.existsById(id)) {
			throw new EntityNotFoundException("Cliente com ID " + id + " não encontrado");
		}

		clienteRepository.deleteById(id);
	}

	private Endereco buscarOuCriarEndereco(String cep) {
		return enderecoRepository.findById(cep).orElseGet(() -> {
			Endereco novoEndereco = viaCepService.consultarCep(cep);
			novoEndereco.setCep(cep);
			return enderecoRepository.save(novoEndereco);
		});
	}

	private Cliente salvarClienteDTOComCep(ClienteDTO clienteDTO) {
		Endereco endereco = buscarOuCriarEndereco(clienteDTO.getCep());
		return ConverteDTOParaCliente(clienteDTO, endereco);
	}

	private Cliente ConverteDTOParaCliente(ClienteDTO clienteDTO, Endereco endereco) {
		Cliente cliente = new Cliente();
		cliente.setNome(clienteDTO.getNome());
		cliente.setEndereco(endereco);

		return cliente;
	}

	private ClienteDTO converteClienteParaDTO(Cliente cliente) {
		ClienteDTO clienteDTO = new ClienteDTO();
		clienteDTO.setId(cliente.getId());
		clienteDTO.setNome(cliente.getNome());
		clienteDTO.setCep(cliente.getEndereco().getCep());

		EnderecoDTO enderecoDTO = new EnderecoDTO();
		Endereco endereco = cliente.getEndereco();
		enderecoDTO.setLogradouro(endereco.getLogradouro());
		enderecoDTO.setComplemento(endereco.getComplemento());
		enderecoDTO.setBairro(endereco.getBairro());
		enderecoDTO.setLocalidade(endereco.getLocalidade());
		enderecoDTO.setUf(endereco.getUf());

		clienteDTO.setEnderecoDTO(enderecoDTO);
		return clienteDTO;
	}
}
