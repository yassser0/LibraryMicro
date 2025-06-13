function selectPayment(method) {
            // Retirer la sélection précédente
            document.querySelectorAll('.payment-method').forEach(el => {
                el.classList.remove('selected');
            });
            
            // Sélectionner la nouvelle méthode
            document.querySelector(`input[value="${method}"]`).checked = true;
            document.querySelector(`input[value="${method}"]`).closest('.payment-method').classList.add('selected');
            
            // Afficher/masquer les détails de carte
            const cardDetails = document.getElementById('cardDetails');
            if (method === 'card') {
                cardDetails.classList.add('show');
            } else {
                cardDetails.classList.remove('show');
            }
        }
        
        // Formatage automatique du numéro de carte
        document.addEventListener('DOMContentLoaded', function() {
            const cardInput = document.querySelector('input[placeholder="1234 5678 9012 3456"]');
            if (cardInput) {
                cardInput.addEventListener('input', function(e) {
                    let value = e.target.value.replace(/\s/g, '');
                    let formattedValue = value.replace(/(.{4})/g, '$1 ').trim();
                    if (formattedValue.length > 19) formattedValue = formattedValue.substr(0, 19);
                    e.target.value = formattedValue;
                });
            }
            
            // Formatage de la date d'expiration
            const expInput = document.querySelector('input[placeholder="MM/AA"]');
            if (expInput) {
                expInput.addEventListener('input', function(e) {
                    let value = e.target.value.replace(/\D/g, '');
                    if (value.length >= 2) {
                        value = value.substring(0, 2) + '/' + value.substring(2, 4);
                    }
                    e.target.value = value;
                });
            }
        });
        
        // Gestion du formulaire
        document.getElementById('paymentForm').addEventListener('submit', function(e) {
            e.preventDefault();
            
            // Vérifier qu'une méthode de paiement est sélectionnée
            const selectedPayment = document.querySelector('input[name="payment"]:checked');
            if (!selectedPayment) {
                alert('Veuillez sélectionner une méthode de paiement');
                return;
            }
            
            // Simulation du traitement de paiement
            const submitBtn = document.querySelector('.btn-success');
            submitBtn.innerHTML = '⏳ Traitement en cours...';
            submitBtn.disabled = true;
            
            setTimeout(() => {
                alert('🎉 Commande confirmée ! Vous recevrez un email de confirmation.');
                submitBtn.innerHTML = '✅ Commande confirmée';
            }, 2000);
        });