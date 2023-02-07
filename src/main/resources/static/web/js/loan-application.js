const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            data: [],
            client: {},
            accounts: [],
            accountDestinatary: "Destinatary Account...",
            amount: [],
            loans: [],
            selectedLoan: "Select Loan...",
            loanApplication: [],
            payment: [],
        }
    },
    created() {
        this.getLoans()
        this.getData();
    },
    mounted() {
    },
    methods: {
        getLoans() {
            axios.get("/api/loans")
                .then(response => response)
                .then(data => {
                    this.loans = data.data

                })
                .catch(error => console.error(error))
        },
        getData() {

            axios.get("/api/clients/current")
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

                    console.log(this.accounts)
                    console.log(this.loans)


                })
                .catch(error => console.error(error))
        },
        appliLoan() {
            console.log(this.loanApplication[0].id)
            console.log(this.amount)
            console.log(this.payment)
            console.log(this.accountDestinatary)
            axios.post("/api/loans", {
                id: this.loanApplication[0].id,
                amount: this.amount,
                payment: this.payment,
                account: this.accountDestinatary
            })
                .then(response => {
                    Swal.fire({
                        title: 'Loan applied',
                        icon: 'success',
                        text: `${response.data}`,
                        customClass: {
                            popup: "text-white borderRadius-15",
                        }
                    })

                })
                .catch(error => {
                    Swal.fire({
                        title: 'Loan error!',
                        icon: 'error',
                        text: `${error.response.data}`,
                        customClass: {
                            popup: "text-white borderRadius-15",
                        }
                    })
                })
        }
    },
    computed: {
        getLoan() {
            this.loanApplication = this.loans.filter(loan => loan.name.includes(this.selectedLoan))
        }
    }
}).mount('#app')