function selectPayment(method) {
    // Retirer la sélection précédente
    document.querySelectorAll('.payment-method').forEach(el => {
        el.classList.remove('selected');
    });

    // Sélectionner la nouvelle méthode
    const radio = document.querySelector(`input[value="${method}"]`);
    if (radio) {
        radio.checked = true;
        radio.closest('.payment-method').classList.add('selected');
    }

    // Afficher/masquer les détails de carte
    const cardDetails = document.getElementById('cardDetails');
    if (method === 'card') {
        cardDetails.classList.add('show');
    } else {
        cardDetails.classList.remove('show');
    }
}

// Formatage automatique du numéro de carte
document.addEventListener('DOMContentLoaded', function () {
    const cardInput = document.querySelector('input[placeholder="1234 5678 9012 3456"]');
    if (cardInput) {
        cardInput.addEventListener('input', function (e) {
            let value = e.target.value.replace(/\s/g, '');
            let formattedValue = value.replace(/(.{4})/g, '$1 ').trim();
            if (formattedValue.length > 19) formattedValue = formattedValue.substr(0, 19);
            e.target.value = formattedValue;
        });
    }

    // Formatage de la date d'expiration
    const expInput = document.querySelector('input[placeholder="MM/AA"]');
    if (expInput) {
        expInput.addEventListener('input', function (e) {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length >= 2) {
                value = value.substring(0, 2) + '/' + value.substring(2, 4);
            }
            e.target.value = value;
        });
    }

    // Gestion du formulaire - ne pas bloquer le submit si tout est bon
    const form = document.getElementById('paymentForm');
    form.addEventListener('submit', function (e) {
        const selectedPayment = document.querySelector('input[name="paymentMethod"]:checked');
        if (!selectedPayment) {
            e.preventDefault(); // Ne pas soumettre si aucune méthode
            alert('Veuillez sélectionner une méthode de paiement');
            return;
        }

        // Optionnel : feedback visuel
        const submitBtn = document.querySelector('.btn-success');
        submitBtn.innerHTML = '⏳ Traitement...';
        submitBtn.disabled = true;
    });
});
