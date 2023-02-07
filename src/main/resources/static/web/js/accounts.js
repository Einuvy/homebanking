const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            id: new URLSearchParams(location.search).get('id'),
            data: [],
            client: {},
            accounts: [],
            ordByIdAccounts: [],
            transactions: [],
            transactionButtonClass: ["button-section", "d-flex", "align-items-center"],
            loans: [],
            type: "",
            color: "",
            loanTopay: "",
            loanPayAccount: "",
            loanPayAmount: "",
            loanSelected: "",
        }
    },
    created() {
        this.getData();
    },
    mounted() {
        const body = document.querySelector("body"),
            sidebar = body.querySelector("nav"),
            toggle = body.querySelector(".toggle"),
            modeSwitch = body.querySelector(".toggle-switch"),
            modeText = body.querySelector(".mode-text"),
            mainContent = body.querySelector(".main-accounts")

        toggle.addEventListener("click", () => {
            sidebar.classList.toggle("close");
        });
        modeSwitch.addEventListener("click", () => {
            body.classList.toggle("dark");
            mainContent.classList.toggle("dark")

            if (body.classList.contains("dark")) {
                modeText.innerText = "Light mode";
            } else {
                modeText.innerText = "Dark mode";
            }
        });

    },
    methods: {
        getData() {
            axios.get(`/api/clients/current`)
                .then(response => {
                    this.data = response.data
                    return this.data
                })
                .then(data => {
                    let USDollar = new Intl.NumberFormat("en-US",
                        {
                            style: "currency",
                            currency: "USD",
                        });
                    this.client = data;
                    this.accounts = this.client.accounts;
                    this.accounts.map(account => account.balance = USDollar.format(account.balance))
                    this.loans = this.client.loans;
                    this.loans.map(loan => loan.amount = USDollar.format(loan.amount))
                    console.log(this.loans);
                    this.ordByIdAccounts = this.accounts.map(accounts => accounts).sort((a, b) => b.id - a.id);
                    console.log(this.ordByIdAccounts);
                    let aux = [];
                    this.accounts.forEach(account => {
                        account.transactions.forEach(transaction => aux.push(transaction))
                    });
                    this.transactions = aux.map(transaction => transaction).sort((a, b) => b.id - a.id);
                    this.transactions.map(transaction => transaction.amount = USDollar.format(transaction.amount))
                    console.log(this.transactions);
                })
                .catch(error => console.error(error))
        },
        backOut() {
            this.transactionButtonClass.push("animate__animated");
            this.transactionButtonClass.push("animate__backOutRight");
        },
        logOut() {
            axios.post("/api/logout")
                .then((response) => {
                    console.log(response.request.responseURL);
                    window.location.href = response.request.responseURL
                })
        },
        createAccount() {
            axios.post('/api/clients/current/accounts')
                .then(response => {
                    console.log(response)
                    location.href = ""
                })
                .catch(error => console.log(error))
        },
        sweetModal() {
            Swal.fire({
                didOpen: (event) => {
                    document.querySelector('.swal2-confirm').addEventListener("click", () => {
                        this.createCard()
                    })
                },
                title: 'Create a card',
                html: `
                <form>
                <select class="type form-select form-select-lg mb-3" aria-label=".form-select-lg example">
                    <option selected>Select your card type</option>
                    <option value="CREDIT">Credit</option>
                    <option value="DEBIT">Debit</option>
                </select>
                <select class="color form-select form-select-lg mb-3" aria-label=".form-select-lg example">
                    <option selected>Select your card color</option>
                    <option value="SILVER">Silver</option>
                    <option value="GOLD">Gold</option>
                    <option value="TITANIUM">Titanium</option>
                </select>
                </form>
                `,
                inputPlaceholder: 'Select a ',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Create card',
                customClass: {
                    popup: "text-white borderRadius-15",
                },
                preConfirm: () => {
                    return [
                        this.type = document.querySelector('.type').value,
                        this.color = document.querySelector('.color').value,
                    ]
                }
            }).then((result) => {
            })
        },
        createCard() {
            axios.post('/api/clients/current/cards',
                "color=" + this.color +
                "&type=" + this.type)
                .then(response => {
                    if (response.status.toString() == "201") {
                        Swal.fire({
                            icon: 'success',
                            title: 'Created!!!',
                            text: response.data,
                            customClass: {
                                popup: "text-white borderRadius-15",
                            },
                        })
                    }

                })
                .catch(error => {
                    console.log(error);
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: error.response.data,
                        customClass: {
                            popup: "text-white borderRadius-15",
                        },
                    })
                })
        },
        transactionDetails(transactionDetails) {
            Swal.fire({
                title: transactionDetails.type,
                html: `
                <div>
                    <p>Description: ${transactionDetails.description}</p>
                    <h4>${transactionDetails.amount}</h4>
                    <span>Date: ${transactionDetails.date}</span>
                </div>
                `,
                confirmButtonColor: '#3085d6',
                confirmButtonText: 'Okay!',
                customClass: {
                    popup: "text-white borderRadius-15",
                }
            })
        },
        payLoanCuote() {
            Swal.fire({
                didOpen: (event) => {
                    const $selectLoantopay = document.querySelector(".loantopay");
                    let templateLoan = "<option selected>Select loan...</option>";
                    this.loans.forEach(loan => templateLoan += `
                    <option value="${loan.name}">${loan.name}</option>
                    `);
                    $selectLoantopay.innerHTML = templateLoan;

                    const $selectPayAccount = document.querySelector(".payaccount");
                    let template = "<option selected>Pay Account...</option>";
                    this.accounts.forEach(account => template += `
                    <option value="${account.number}">${account.number}</option>
                    `);
                    $selectPayAccount.innerHTML = template;
                    document.querySelector('.swal2-confirm').addEventListener("click", () => {
                        this.areYouSure()
                    })
                },
                title: 'Select loan to pay',
                html: `
                <form>
                <select class="loantopay form-select form-select-lg mb-3" aria-label=".form-select-lg example">
                    <option selected>Select loan</option>
                </select>
                <select class="payaccount form-select form-select-lg mb-3" aria-label=".form-select-lg example">
                    <option selected>Select loan</option>
                </select>
                </form>
                `,
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Pay a loan payment',
                customClass: {
                    popup: "text-white borderRadius-15",
                },
                preConfirm: () => {
                    return [
                        this.loanSelected = document.querySelector('.loantopay').value,
                        this.loanPayAccount = document.querySelector(".payaccount").value,

                    ]
                }
            }).then((result) => {
            })
        },
        areYouSure() {
            this.loanTopay = this.loans.filter(loan => loan.name.includes(this.loanSelected))
            let myAmount = parseFloat(this.loanTopay[0].amount.replace("$", "").replace(",", ""));
            let myPayments = this.loanTopay[0].payments;
            this.loanPayAmount = (myAmount / myPayments).toFixed(2)
            Swal.fire({
                didOpen: (event) => {
                    document.querySelector('.swal2-confirm').addEventListener("click", () => {
                        this.payPayment(this.loanPayAccount, this.loanPayAmount, this.loanTopay[0].id)
                    })
                },
                icon: 'question',
                title: 'Are you sure?',
                text: `The account payment is ${this.loanPayAccount}, loan amount is ${this.loanPayAmount}
                and loan to pay resume is: Name: ${this.loanTopay[0].name} - Payments ${this.loanTopay[0].payments} -  ${this.loanTopay[0].amount}`,
            })
        },
        payPayment(account, amount, id) {
            axios.post("/api/payloan",
                "clientLoanId="+id +
                "&amount="+amount +
                "&account="+account)
                .then(response => {
                    Swal.fire({
                        icon: 'success',
                        title: 'Thanks to pay!!!',
                        text: response.data,
                        customClass: {
                            popup: "text-white borderRadius-15",
                        },
                    })
                })
                .catch(error => {
                    console.log(error);
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: error.response.data,
                        customClass: {
                            popup: "text-white borderRadius-15",
                        },
                    })
                })
        },
        deleteAccount(number){
            Swal.fire({
                didOpen: (event) => {
                    document.querySelector('.swal2-confirm').addEventListener("click", () => {
                        console.log(number)
                        axios.delete("/api/clients/current/account", {
                            params:{
                                accountNumber: number
                            }
                        })
                        .then(data => {
                            console.log(data)
                            location.reload()
                        })
                        /* .then(location.reload()) */
                        .catch(error => {
                            console.log(error);
                            Swal.fire({
                                icon: 'error',
                                title: 'Oops...',
                                text: error.response.data,
                                customClass: {
                                    popup: "text-white borderRadius-15",
                                },
                            })
                        })
                    })
                },
                icon: 'question',
                title: 'Are you sure?',
                text: `Delete account ${number}`,
            })
            
        },
    },
    computed: {

    }
}).mount('#app')