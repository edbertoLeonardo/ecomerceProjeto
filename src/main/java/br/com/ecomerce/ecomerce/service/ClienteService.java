package br.com.ecomerce.ecomerce.service;

import br.com.ecomerce.ecomerce.dto.ClienteDto;
import br.com.ecomerce.ecomerce.dto.ClienteNewDto;
import br.com.ecomerce.ecomerce.exception.DataIntegrityException;
import br.com.ecomerce.ecomerce.exception.ObjectNotFoundException;
import br.com.ecomerce.ecomerce.model.Cidade;
import br.com.ecomerce.ecomerce.model.Cliente;
import br.com.ecomerce.ecomerce.model.Endereco;
import br.com.ecomerce.ecomerce.model.enums.TipoCliente;
import br.com.ecomerce.ecomerce.repositories.CidadeRepository;
import br.com.ecomerce.ecomerce.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ClienteService {



    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    public Cliente findById(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrada com ID: " + id
                        +   ", Tipo: " + Cliente.class.getName()));
    }


    public Cliente update(Cliente cliente){
       Cliente newCliente =  findById(cliente.getId());
        updateData(newCliente, cliente);
        return clienteRepository.save(newCliente);
    }

    public Cliente insert(Cliente cliente){
        cliente.setId(null);
        return clienteRepository.save(cliente);
    }

    private void updateData(Cliente newCliente, Cliente cliente) {
        newCliente.setNome(cliente.getNome());
        newCliente.setEmail(cliente.getEmail());
    }

    public void delete(Integer id){
       findById(id);
        try {
            clienteRepository.deleteById(id);
        }catch (DataIntegrityViolationException e){
            throw new DataIntegrityException("Não é possível excluir um cliente com peididos");
        }
    }

    public List<Cliente> findAll(){
        return clienteRepository.findAll();
    }

    public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        return clienteRepository.findAll(pageRequest);
    }

    public Cliente fromDto(ClienteDto clienteDto){
        return new  Cliente(clienteDto.getId(), clienteDto.getNome(), clienteDto.getEmail(), null, null);
    }

    public Cliente fromDTO(ClienteNewDto clienteNewDto) {
        Cliente cliente = new Cliente(null, clienteNewDto.getNome(), clienteNewDto.getEmail(), clienteNewDto.getCpfOuCnpj(), TipoCliente.toEnum(clienteNewDto.getTipo()));
        Cidade cidade = cidadeRepository.getReferenceById(clienteNewDto.getCidadeId());
        Endereco endereco = new Endereco(null, clienteNewDto.getLogradouro(), clienteNewDto.getNumero(), clienteNewDto.getComplemento(), clienteNewDto.getBairro(), clienteNewDto.getCep(), cliente, cidade);
        cliente.getEnderecosList().add(endereco);
        cliente.getTelefones().add(clienteNewDto.getTelefone());
        if (clienteNewDto.getTelefone2()!=null) {
            cliente.getTelefones().add(clienteNewDto.getTelefone2());
        }
        if (clienteNewDto.getTelefone3()!=null) {
            cliente.getTelefones().add(clienteNewDto.getTelefone3());
        }
        return cliente;
    }
}
