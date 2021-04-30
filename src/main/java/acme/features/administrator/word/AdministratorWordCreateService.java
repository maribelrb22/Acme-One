package acme.features.administrator.word;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.spam.Spam;
import acme.entities.spam.Word;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Administrator;
import acme.framework.services.AbstractCreateService;
import acme.repositories.SpamRepository;

@Service
public class AdministratorWordCreateService implements AbstractCreateService<Administrator, Word>{

	@Autowired
	protected AdministratorWordRepository repository;
	
	@Autowired
	protected SpamRepository spamRepo;
	
	@Override
	public boolean authorise(final Request<Word> request) {
		assert request != null;
		return true;
	}

	@Override
	public void bind(final Request<Word> request, final Word entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		
		request.bind(entity, errors);
	}

	@Override
	public void unbind(final Request<Word> request, final Word entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
		request.unbind(entity, model, "word");
	}

	@Override
	public Word instantiate(final Request<Word> request) {
		assert request != null;
		Word result;
		
		result  = new Word();
		result.setWord("estonovauwu");
		
		return result;
	}

	@Override
	public void validate(final Request<Word> request, final Word entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
	
	}

	@Override
	public void create(final Request<Word> request, final Word entity) {
		assert request != null;
		assert entity != null;
		final Spam spam = this.repository.findSpam();
		final Collection<Word> spamWords = spam.getSpamWords();
		this.repository.save(entity);
		spamWords.add(entity);
		this.spamRepo.save(spam);
	
	}

}