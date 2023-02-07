const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            id: new URLSearchParams(location.search).get('id'),
            data: [],
            client: {},
            accounts: [],
            account: {},
            transactions: [],
            availableAccounts: [],
            accountDestinatary: "Destinatary Account...",
            description: "",
            amount: "",
            sendOtherAccountOrigin: "Origin Account...",
            sendOtherAccount: "",
            sendOtherAmount: "",
            sendOtherDescription: "",
            recipientClient: {},
            initDate: "",
            endDate: "",
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
                    this.account = this.accounts.find(account => (account.id.toString()) == this.id)
                    this.transactions = this.account.transactions.map(transaction => transaction).sort((a, b) => b.id - a.id);
                    this.transactions.map(transaction => transaction.amount = USDollar.format(transaction.amount))
                    this.availableAccounts = this.accounts.filter(account => (account.id.toString()) != this.id)
                })
                .catch(error => console.error(error))
        },
        logOut() {
            axios.post("/api/logout")
                .then((response) => {
                    console.log("signed out!!");
                    window.location.href = response.request.responseURL
                })
        },
        sweetTransaction() {
            Swal.fire({
                didOpen: (event) => {
                    document.querySelector('.myself').addEventListener("click", () => {
                        this.myselfTransfer()
                    }),
                        document.querySelector('.other').addEventListener("click", () => {
                            this.otherTransfer()
                        })
                },
                title: "Welcome!",
                html: `
                <div>
                    <h5>Who do you want to transfer?</h5>
                    <div class="d-flex justify-content-around">
                        <button type="button" class="myself btn btn-success">Myself</button>
                        <button type="button" class="other btn btn-info">Other</button>
                    </div>
                </div>
                `,
                showConfirmButton: false,
                customClass: {
                    popup: "text-white borderRadius-15",
                },
            })
        },
        myselfTransfer() {
            Swal.fire({
                didOpen: (e) => {
                    const $selectDestinatary = document.querySelector(".destinatary");
                    let template = "<option selected>Destinatary Account...</option>";
                    this.availableAccounts.forEach(account => template += `
                    <option value="${account.number}">${account.number}</option>
                    `);
                    $selectDestinatary.innerHTML = template;
                    document.querySelector('.swal2-confirm').addEventListener("click", () => {
                        this.sendTransfer();
                    })
                },
                title: "You selected transfers for yourself",
                html: `
                <div id="app">
                <div class="m-4 d-flex flex-column">
                  <div>
                    <h5>Select destination account</h5>
                    <select class="destinatary form-select form-select-lg mb-3" aria-label=".form-select-lg example">
                    </select>
                  </div>
                  <div>
                    <h5>Select Amount</h5>
                    <div class="d-flex">
                      <span class="input-group-text">$</span>
                      <input type="number" class="amount form-control form-select-lg mb-3" aria-label=".form-select-lg example">
                      </input>
                    </div>
                  </div>
                  <div class="mb-3">
                    <h5>Description</h5>
                    <input type="text"
                      class="description form-control" aria-describedby="helpId">
                    <small id="helpId" class="form-text text-muted"></small>
                  </div>
                </div>
                </div>

                `,
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Create Transfer',
                customClass: {
                    popup: "text-white borderRadius-15",
                },
                preConfirm: () => {
                    return [
                        this.accountDestinatary = document.querySelector('.destinatary').value,
                        this.description = document.querySelector(".description").value,
                        this.amount = document.querySelector(".amount").value,
                        console.log(" " + this.accountDestinatary + " " + this.description + " " + this.amount)
                    ]
                }
            })
        },
        otherTransfer() {
            Swal.fire({
                didOpen: (e) => {
                    document.querySelector('.swal2-confirm').addEventListener("click", () => {
                        this.sendTransfer();
                    })
                },
                title: "You selected transfers for yourself",
                html: `
                <div id="app">
                <div class="m-4 d-flex flex-column">
                    <div>
                        <h5>Select destinatary account</h5>
                        <input type="text"
                            class="form-control destinatary" aria-describedby="helpId" v-model="sendOtherAccount">
                        <small id="helpId" class="form-text text-muted"></small>
                    </div>
                  <div>
                    <h5>Select Amount</h5>
                    <div class="d-flex">
                      <span class="input-group-text">$</span>
                      <input type="number" class="amount form-control form-select-lg mb-3" aria-label=".form-select-lg example">
                      </input>
                    </div>
                  </div>
                  <div class="mb-3">
                    <h5>Description</h5>
                    <input type="text"
                      class="description form-control" aria-describedby="helpId">
                    <small id="helpId" class="form-text text-muted"></small>
                  </div>
                </div>
                </div>

                `,
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Create Transfer',
                customClass: {
                    popup: "text-white borderRadius-15",
                },
                preConfirm: () => {
                    return [
                        this.accountDestinatary = document.querySelector('.destinatary').value,
                        this.description = document.querySelector(".description").value,
                        this.amount = document.querySelector(".amount").value,
                        console.log(" " + this.accountDestinatary + " " + this.description + " " + this.amount)
                    ]
                }
            })
        },
        sendTransfer() {
            this.accountDestinatary = this.accountDestinatary.toUpperCase();
            if (this.accountDestinatary === "Destinatary Account..." || this.accountDestinatary === "" || this.description === "" || this.amount === "" || this.amount === 0) {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: "Yo need complete all fields in the form",
                    customClass: {
                        popup: "text-white borderRadius-15",
                    },
                })
            } else {
                axios.post('/api/transactions',
                    "amount=" + this.amount +
                    "&description=" + this.description +
                    "&numberOrigin=" + this.account.number +
                    "&numberRecipients=" + this.accountDestinatary)
                    .then(response => {
                        Swal.fire({
                            didOpen: (event) => {
                                document.querySelector('.swal2-confirm').addEventListener("click", () => {
                                    location.href = "";
                                })
                            },
                            title: 'Transfer send!',
                            icon: 'success',
                            text: `${response.data}`,
                            customClass: {
                                popup: "text-white borderRadius-15",
                            }
                        })
                    })
                    .catch(error => {
                        Swal.fire({
                            title: 'Transfer error!',
                            icon: 'error',
                            text: `${error.response.data}`,
                            customClass: {
                                popup: "text-white borderRadius-15",
                            }
                        })
                        console.log(error);
                    })
            }
        },
        transactionDetails(transactionDetails) {
            Swal.fire({
                title: transactionDetails.type,
                html: `
                <div>
                    <p>Description: ${transactionDetails.description}</p>
                    <h4> ${transactionDetails.amount}</h4>
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
        createPDF() {
            Swal.fire({
                didOpen: (e) => {
                    document.querySelector('.swal2-confirm').addEventListener("click", () => {
                        console.log(this.initDate);
                        console.log(this.endDate);
                        axios.get('/api/accounts/pdf/generate', {
                            params: {
                                initDate: this.initDate + "T00:00:00",
                                endDate: this.endDate + "T00:00:00",
                                accountNumber: this.account.number
                            },
                            responseType: 'blob'
                        })
                            .then(response => {
                                console.log()
                                let contentDisposition = response.headers['content-disposition']
                                let startIndex = contentDisposition.indexOf("transactions");
                                let fileName = contentDisposition.substring(startIndex);
                                const url = window.URL.createObjectURL(new Blob([response.data]));
                                const link = document.createElement('a');
                                link.href = url;
                                link.setAttribute('download', fileName);
                                document.body.appendChild(link);
                                link.click();
                            })
                            .catch(error => {
                                console.error(error)
                                Swal.fire({
                                    icon: 'error',
                                    title: 'Oops...',
                                    text: error.response.data,
                                    customClass: {
                                        popup: "text-white borderRadius-15",
                                    },
                                })
                            });
                    })
                },
                title: "You selected date to PDF transaction",
                html: `
                <div class="m-4 d-flex flex-column">
                    <div>
                        <h5>Select init date</h5>
                        <input type="date"
                            class="form-control initDate" aria-describedby="helpId">
                        <small id="helpId" class="form-text text-muted"></small>
                    </div>
                    <div>
                    <h5>Select end date</h5>
                    <input type="date"
                        class="form-control endDate" aria-describedby="helpId">
                    <small id="helpId" class="form-text text-muted"></small>
                </div>
                </div>

                `,
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Create Transfer',
                customClass: {
                    popup: "text-white borderRadius-15",
                },
                preConfirm: () => {
                    return [
                        this.initDate = document.querySelector('.initDate').value,
                        this.endDate = document.querySelector(".endDate").value,
                        console.log(" " + this.initDate + " " + this.endDate)
                    ]
                }
            })





            /* axios.get('/api/accounts/pdf/generate?initDate=2022-12-24T10:30:00&endDate=2023-02-02T10:30:00&accountNumber=VIN004')
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
                   
                }) */

        },
    },
    computed: {

    }
}).mount('#app')